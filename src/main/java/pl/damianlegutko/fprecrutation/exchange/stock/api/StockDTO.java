package pl.damianlegutko.fprecrutation.exchange.stock.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTO {
    private String companyCode;
    private Long stockAmount;

    public Company getCompany() throws StockCodeOutsideEnumException {
        return Company.parseStockCode(companyCode);
    }

    public void increaseStockAmount(Long increaseByValue){
        this.stockAmount = Long.sum(this.stockAmount, increaseByValue);
    }

    public void decreaseStockAmount(Long decreaseByValue) throws UserHaveNotEnoughStocksException {
        if (this.stockAmount.compareTo(decreaseByValue) < 0) throw new UserHaveNotEnoughStocksException();

        this.stockAmount -= decreaseByValue;
    }
}