package pl.damianlegutko.fprecrutation.exchange.stock.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class StockCodeOutsideEnumException extends StockException {

    public StockCodeOutsideEnumException(String companyCode) {
        super(Messages.get("exchange.exp.stockOutsideEnum", companyCode));
        this.errorCode = ErrorCode.STOCK_CODE_OUTSIDE_ENUM;
    }
}