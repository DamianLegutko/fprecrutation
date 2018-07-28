package pl.damianlegutko.fprecrutation.exchange.stock.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class StockNotExistsException extends StockException {

    public StockNotExistsException(String companyCode) {
        super(Messages.get("stock.exp.notExists", companyCode));
        this.errorCode = ErrorCode.STOCK_NOT_EXISTS;
    }
}