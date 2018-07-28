package pl.damianlegutko.fprecrutation.exchange.stock.exceptions;

import pl.damianlegutko.fprecrutation.BaseException;

public class StockException extends BaseException {
    
    public StockException(String errorMessage) {
        super(errorMessage);
    }
}