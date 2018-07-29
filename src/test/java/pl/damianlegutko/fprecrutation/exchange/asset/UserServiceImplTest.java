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
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    private static final String USER_NAME = "user";
    private static final String USER_PASSWORD = "QWE123zxc";
    private static final BigDecimal START_WALLET = new BigDecimal(1000000);
    private String testUser = null;

    @Before
    public void setUp() {
        testUser = USER_NAME + RandomStringUtils.random(10, true, false);
    }

    @After
    public void closeUp() {
        testUser = null;
    }

    //region Sanity tests
    @Test
    public void signIn_withSuccess_whenUserExists() {
        //given
        UserDTO userDTO = UserDTO.builder()
                            .username(testUser)
                            .password(USER_PASSWORD)
                            .confirmPassword(USER_PASSWORD)
                            .money(START_WALLET)
                            .build();

        //when
        userService.saveUser(userDTO);

//        userService.signin(userDTO.getUsername(), userDTO.getPassword());

        //than
        assertThat(securityService.findLoggedInUsername()).isEqualTo(testUser.toLowerCase());
    }
    //endregion

    //region Null validation tests

    //endregion

    //region Other cases

    //endregion
}