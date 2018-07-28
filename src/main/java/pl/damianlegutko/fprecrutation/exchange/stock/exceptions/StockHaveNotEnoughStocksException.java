package pl.damianlegutko.fprecrutation.exchange.stock.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class StockHaveNotEnoughStocksException extends StockException {

    public StockHaveNotEnoughStocksException(Long userStockAmount, String companyName, Long decreaseByValue) {
        super(Messages.get("stock.exp.haveNotEnoughStocks", userStockAmount, companyName,  decreaseByValue));
        this.errorCode = ErrorCode.STOCK_HAVE_NOT_ENOUGH_STOCKS;
    }
}