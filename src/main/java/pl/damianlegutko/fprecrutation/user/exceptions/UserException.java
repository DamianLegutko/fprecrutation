package pl.damianlegutko.fprecrutation.user.exceptions;

import pl.damianlegutko.fprecrutation.commonExceptions.BaseException;

public class UserException extends BaseException {
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}