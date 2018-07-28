package pl.damianlegutko.fprecrutation.exchange.asset.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class UserHaveNotEnoughStocksException extends AssetException {

    public UserHaveNotEnoughStocksException(String companyName, Long decreaseByValue, Long userStockAmount) {
        super(Messages.get("user.exp.haveNotEnoughStocks", companyName, decreaseByValue, userStockAmount));
        this.errorCode = ErrorCode.USER_HAVE_NOT_ENOUGH_STOCKS;
    }

    public UserHaveNotEnoughStocksException(Long decreaseByValue, Long userStockAmount) {
        super(Messages.get("asset.exp.userHaveNotEnoughStocks", decreaseByValue, userStockAmount));
        this.errorCode = ErrorCode.USER_HAVE_NOT_ENOUGH_STOCKS;
    }

    public UserHaveNotEnoughStocksException(String companyName) {
        super(Messages.get("user.exp.haveNotStocks",companyName));
        this.errorCode = ErrorCode.USER_HAVE_NOT_ENOUGH_STOCKS;
    }
}