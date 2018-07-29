package pl.damianlegutko.fprecrutation.exchange.asset;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.configuration.AppTestConfig;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.api.UserAssetsDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.api.UserStock;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.StockService;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.user.SecurityService;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class AssetServiceImplTest {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    @Autowired
    private SecurityService securityService;

    private static final String USER_NAME = "AssetServiceImpl";
    private static final String USER_PASSWORD = "QWE123zxc";
    private static final BigDecimal START_WALLET = new BigDecimal(1000000);
    private static final Company DEFAULT_COMPANY = Company.DL24;
    private String testUser = null;

    @Before
    public void setUp() {
        testUser = USER_NAME + RandomStringUtils.random(10,true,false);

        UserDTO userDTO = UserDTO.builder()
                .money(START_WALLET)
                .username(testUser)
                .password(USER_PASSWORD)
                .build();

        userService.saveUser(userDTO);

        securityService.signin(testUser, USER_PASSWORD);
    }

    @After
    public void closeUp() {
        testUser = null;
    }

    //region Sanity tests
    @Test
    public void buyAssetByUser_userStock_isIncreasing() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        assetService.buyAssetByUser(assetToBuy);

        UserAssetsDTO userAssetsAfter = assetService.getUserAssetsByUserName(testUser);
        Optional<UserStock> userStockAfter = getUserStockFromAssetsByCompany(userAssetsAfter, DEFAULT_COMPANY);

        //than
        assertThat(userStockAfter.get().getStockAmount()).isEqualTo(stockCountToBuy);
    }

    @Test
    public void buyAssetByUser_walletCash_isDecreasing() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        assetService.buyAssetByUser(assetToBuy);

        UserAssetsDTO userAssetsAfter = assetService.getUserAssetsByUserName(testUser);

        //than
        BigDecimal stockCost = pricePerStock.multiply(new BigDecimal(stockCountToBuy));
        assertThat(userAssetsAfter.getMoney().setScale(2)).isEqualTo(START_WALLET.subtract(stockCost).setScale(2));
    }

    @Test
    public void buyAssetByUser_stockAmountInStockExchange_isDecreasing() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        StockDTO stockBefore = stockService.findStockByCompany(DEFAULT_COMPANY.toString());

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        assetService.buyAssetByUser(assetToBuy);

        StockDTO stockAfter = stockService.findStockByCompany(DEFAULT_COMPANY.toString());

        //than
        assertThat(stockAfter.getStockAmount()).isEqualTo(stockBefore.getStockAmount() - stockCountToBuy);
    }

    @Test
    public void sellAssetByUser_userStock_isDecreasing() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuyAndSell = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        assetService.buyAssetByUser(assetToBuyAndSell);

        //when
        assetService.sellAssetByUser(assetToBuyAndSell);

        UserAssetsDTO userAssetsAfter = assetService.getUserAssetsByUserName(testUser);
        Optional<UserStock> userStockAfter = getUserStockFromAssetsByCompany(userAssetsAfter, DEFAULT_COMPANY);

        //than
        assertThat(userStockAfter.get().getStockAmount()).isEqualTo(0);
    }

    @Test
    public void sellAssetByUser_walletCash_isIncreasing() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuyAndSell = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        assetService.buyAssetByUser(assetToBuyAndSell);

        //when
        assetService.sellAssetByUser(assetToBuyAndSell);

        UserAssetsDTO userAssetsAfter = null;
        userAssetsAfter = assetService.getUserAssetsByUserName(testUser);
        //than
        assertThat(userAssetsAfter.getMoney().setScale(2)).isEqualTo(START_WALLET.setScale(2));
    }

    @Test
    public void sellAssetByUser_stockAmountInStockExchange_isIncreasing() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        StockDTO stockBefore = stockService.findStockByCompany(DEFAULT_COMPANY.toString());

        AssetDTO assetToBuyAndSell = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        assetService.buyAssetByUser(assetToBuyAndSell);

        //when
        assetService.sellAssetByUser(assetToBuyAndSell);

        StockDTO stockAfter = stockService.findStockByCompany(DEFAULT_COMPANY.toString());

        //than
        assertThat(stockAfter.getStockAmount()).isEqualTo(stockBefore.getStockAmount());
    }
    //endregion

    //region Null validation tests
    @Test
    public void buyAssetByUser_throwEmptyFieldException_whenCompanyCodeIsNull() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(null)
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [companyCode].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void buyAssetByUser_throwEmptyFieldException_whenStockAmountIsNull() {
        //given
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(null)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [stockAmount].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void buyAssetByUser_throwEmptyFieldException_whenStockPriceIsNull() {
        //given
        Long stockCountToBuy = 7L;

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(null)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [stockPrice].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void buyAssetByUser_throwEmptyFieldException_whenUserNameIsNull() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(null)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [userName].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void sellAssetByUser_throwEmptyFieldException_whenCompanyCodeIsNull() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(null)
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [companyCode].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void sellAssetByUser_throwEmptyFieldException_whenStockAmountIsNull() {
        //given
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(null)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [stockAmount].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void sellAssetByUser_throwEmptyFieldException_whenStockPriceIsNull() {
        //given
        Long stockCountToBuy = 7L;

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(null)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [stockPrice].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void sellAssetByUser_throwEmptyFieldException_whenUserNameIsNull() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(null)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because buying operation should require not null [userName].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }
    //endregion

    //region Other cases
    @Test
    public void buyAssetByUser_throwStockCodeOutsideEnumException_whenSetNotExistsEnumCode() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode("notExistsEnumCode")
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation require [companyCode] value from Company enum.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(StockCodeOutsideEnumException.class);
        }
    }

    @Test
    public void buyAssetByUser_throwConstraintViolationException_whenStockAmountIsNegative() {
        //given
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(-1L)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation require [stockAmount] is positive value.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessage("takeMoneyFromUser.moneyAmount: must be greater than or equal to 0");
        }
    }

    @Test
    public void buyAssetByUser_throwConstraintViolationException_whenStockPriceIsNegative() {
        //given
        Long stockCountToBuy = 7L;

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(new BigDecimal(-1L))
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation require [stockPrice] is positive.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessage("takeMoneyFromUser.moneyAmount: must be greater than or equal to 0");
        }
    }

    @Test
    public void buyAssetByUser_throwUserHaveNotEnoughMoneyException_whenDoNotHaveEnoughMoney() {
        //given
        Long stockCountToBuy = 100L;

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(new BigDecimal(1000000L))
                .userName(testUser)
                .build();

        //when
        try {
            assetService.buyAssetByUser(assetToBuy);
            fail("Fail because buying operation require user have enough money in their wallet.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(UserHaveNotEnoughMoneyException.class);
        }
    }

    @Test
    public void sellAssetByUser_throwStockCodeOutsideEnumException_whenSetNotExistsEnumCode() {
        //given
        Long stockCountToBuy = 7L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode("notExistsEnumCode")
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because selling operation require [companyCode] value from Company enum.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(StockCodeOutsideEnumException.class);
        }
    }

    @Test
    public void sellAssetByUser_throwConstraintViolationException_whenStockAmountIsNegative() {
        //given
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(-1L)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because buying operation require [stockAmount] is positive value.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("giveMoneyToUser.moneyAmount: must be greater than or equal to 0");
        }
    }

    @Test
    public void sellAssetByUser_throwConstraintViolationException_whenStockPriceIsNegative() {
        //given
        Long stockCountToBuy = 7L;

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(new BigDecimal(-1L))
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because buying operation require [stockPrice] is positive value.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("giveMoneyToUser.moneyAmount: must be greater than or equal to 0");
        }
    }

    @Test
    public void sellAssetByUser_throwUserHaveNotEnoughStocksException_whenUserHaveNotEnoughStocks() {
        //given
        Long stockCountToBuy = 1L;
        BigDecimal pricePerStock = new BigDecimal(10);

        AssetDTO assetToBuy = AssetDTO.builder()
                .companyCode(DEFAULT_COMPANY.toString())
                .stockAmount(stockCountToBuy)
                .stockPrice(pricePerStock)
                .userName(testUser)
                .build();

        //when
        try {
            assetService.sellAssetByUser(assetToBuy);
            fail("Fail because selling operation require user have to have enough stocks which try to sell.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(UserHaveNotEnoughStocksException.class);
        }
    }
    //endregion

    private Optional<UserStock> getUserStockFromAssetsByCompany(UserAssetsDTO userAssetsDTO, Company company){
        return userAssetsDTO.getAssetWallet()
                .stream()
                .filter(getUserStockPredicate(company))
                .findFirst();
    }

    private Predicate<UserStock> getUserStockPredicate(Company company) {
        return assetWallet -> assetWallet.getCompanyCode().equals(company.toString());
    }

    @Test
    public void sellAssetByUser() {
    }
}