package pl.damianlegutko.fprecrutation.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;

@Getter
@AllArgsConstructor
public enum Company {
    FP("Future Processing"),
    FPL("FP Lab"),
    PGB("Progress Bar"),
    FPC("FP Coin"),
    FPA("FP Adventure"),
    DL24("Deadline 24");

    private String companyName;

    public static Company parseCompanyCode(String companyCode) throws StockCodeOutsideEnumException {
        try {
            return Company.valueOf(companyCode);
        }
        catch (IllegalArgumentException enumWithThisCodeDontExists){
            throw new StockCodeOutsideEnumException(companyCode);
        }
    }
}