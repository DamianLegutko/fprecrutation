package pl.damianlegutko.fprecrutation.exchange.stock.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class StockAlreadyExistsException extends StockException {

    public StockAlreadyExistsException(String companyName) {
        super(Messages.get("stock.exp.alreadyExists", companyName));
        this.errorCode = ErrorCode.STOCK_ALREADY_EXISTS;
    }
}