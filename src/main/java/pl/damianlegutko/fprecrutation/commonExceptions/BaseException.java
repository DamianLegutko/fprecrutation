package pl.damianlegutko.fprecrutation.commonExceptions;

import lombok.Getter;

@Getter
public class BaseException extends Exception {
    public enum ErrorCode {
        NON_BUSINESS_LOGIC,
        USER_HAVE_NOT_ENOUGH_STOCKS,
        USER_HAVE_NOT_ENOUGH_MONEY,
        USER_ALREADY_EXISTS,
        USER_NOT_EXISTS,
        STOCK_ALREADY_EXISTS,
        STOCK_NOT_EXISTS,
        STOCK_CODE_OUTSIDE_ENUM,
        STOCK_HAVE_NOT_ENOUGH_STOCKS,
        PASSWORD_NOT_MATCH, EMPTY_FIELD
    }

    protected ErrorCode errorCode = ErrorCode.NON_BUSINESS_LOGIC;

    protected String errorMessage;

    public BaseException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }
}