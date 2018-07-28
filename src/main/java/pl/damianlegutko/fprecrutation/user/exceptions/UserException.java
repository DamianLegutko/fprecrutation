package pl.damianlegutko.fprecrutation.user.exceptions;

import pl.damianlegutko.fprecrutation.BaseException;

public class UserException extends BaseException {
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}