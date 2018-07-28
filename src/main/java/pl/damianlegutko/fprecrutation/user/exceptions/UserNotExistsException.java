package pl.damianlegutko.fprecrutation.user.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class UserNotExistsException extends UserException {

    public UserNotExistsException(String userName) {
        super(Messages.get("user.exp.notExists", userName));
        this.errorCode = ErrorCode.USER_NOT_EXISTS;
    }
}
