package pl.damianlegutko.fprecrutation.user.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(String userName) {
        super(Messages.get("user.exp.alreadyExists", userName));
        this.errorCode = ErrorCode.USER_ALREADY_EXISTS;
    }
}
