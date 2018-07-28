package pl.damianlegutko.fprecrutation.exchange.stock.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.Validators;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockHaveNotEnoughStocksException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTO {
    private String companyCode;
    private Long stockAmount;

    public Company getCompany() throws StockCodeOutsideEnumException {
        return Company.parseCompanyCode(companyCode);
    }

    public void increaseStockAmount(Long increaseByValue){
        this.stockAmount = Long.sum(this.stockAmount, increaseByValue);
    }

    public void decreaseStockAmount(Long decreaseByValue) throws StockHaveNotEnoughStocksException, StockCodeOutsideEnumException {
        if (this.stockAmount.compareTo(decreaseByValue) < 0) {
            throw new StockHaveNotEnoughStocksException(this.stockAmount, getCompany().getCompanyName(),  decreaseByValue);
        }

        this.stockAmount -= decreaseByValue;
    }

    public void validateAllFields() throws EmptyFieldException {
        Validators.objectIsNotNull(companyCode, "companyCode");
        Validators.objectIsNotNull(stockAmount, "stockAmount");
    }
}