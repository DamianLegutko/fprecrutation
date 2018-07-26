package pl.damianlegutko.fprecrutation.exchange;

import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;

public enum Company {
    FP,
    FPL,
    PGB,
    FPC,
    FPA,
    DL24;

    public static Company parseStockCode(String stockCode) throws StockCodeOutsideEnumException {
        try {
            return Company.valueOf(stockCode);
        }
        catch (IllegalArgumentException enumWithThisCodeDontExists){
            throw new StockCodeOutsideEnumException();
        }
    }
}