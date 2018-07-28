package pl.damianlegutko.fprecrutation.exchange.stock.exceptions;

import pl.damianlegutko.fprecrutation.commonExceptions.BaseException;

public class StockException extends BaseException {
    
    public StockException(String errorMessage) {
        super(errorMessage);
    }
}