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
import pl.damianlegutko.fprecrutation.exchange.stock.StockService;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockAlreadyExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.user.SecurityService;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class StockServiceImplTest {

    @Autowired
    private StockService stockService;

    private static final Company DEFAULT_COMPANY = Company.DL24;
    private static final Long defualtStockAmount = 100L;

    //region Sanity tests
    @Test
    public void findStockByCompany_companyCode_isCorrect() throws StockCodeOutsideEnumException {
        //given
        //when
        StockDTO receivedStock = stockService.findStockByCompany(DEFAULT_COMPANY.name());

        //than
        assertThat(receivedStock.getCompany().name()).isEqualTo(DEFAULT_COMPANY.name());
    }
    //endregion

    //region Null validation tests
    @Test
    public void findStockByCompany_companyCode_isNull() {
        //given
        //when
        try {
            stockService.findStockByCompany(null);
            fail("Fail because findStockByCompany require [companyCode].");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessageContaining("must not be blank");
        }
    }

    @Test
    public void saveStock_companyCode_isCorrect() throws StockCodeOutsideEnumException {
        //given
        //when
        StockDTO receivedStock = stockService.findStockByCompany(DEFAULT_COMPANY.name());

        //than
        assertThat(receivedStock.getCompany().name()).isEqualTo(DEFAULT_COMPANY.name());
    }
    //endregion

    //region Other cases
    @Test
    public void findStockByCompany_companyCode_isOutOfEnum() {
        //given
        //when
        try {
            stockService.findStockByCompany("NotDefinedCompanyInEnum");
            fail("Fail because findStockByCompany require [companyCode] from Company enum.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getCause()).isInstanceOf(StockCodeOutsideEnumException.class);
        }
    }

    @Test
    public void saveStock_withoutSuccess_whenStockAlreadyExists() {
        //given
        StockDTO stockDTO = StockDTO.builder()
                                .stockAmount(defualtStockAmount)
                                .companyCode(DEFAULT_COMPANY.name())
                                .build();

        //when
        try {
            stockService.saveStock(stockDTO);
            fail("Fail because findStockByCompany require [companyCode].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getCause()).isInstanceOf(StockAlreadyExistsException.class);
        }
    }

    @Test
    public void updateStock_withSuccess_whenStockAmountIs() {
        //given
        Long addStockAmountValue = 1L;

        StockDTO stockDTO = StockDTO.builder()
                .stockAmount(defualtStockAmount)
                .companyCode(DEFAULT_COMPANY.name())
                .build();
        //when
        stockDTO.setStockAmount(stockDTO.getStockAmount() + addStockAmountValue);
        stockService.updateStock(stockDTO);

        //than
        StockDTO receivedStock = stockService.findStockByCompany(stockDTO.getCompanyCode());

        assertThat(receivedStock.getStockAmount()).isEqualTo(stockDTO.getStockAmount());
    }
    //endregion
}