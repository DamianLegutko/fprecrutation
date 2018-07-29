package pl.damianlegutko.fprecrutation.user.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class PasswordNotMatchException extends UserException {

    public PasswordNotMatchException() {
        super(Messages.get("user.exp.passwordNotMatch"));
        this.errorCode = ErrorCode.PASSWORD_NOT_MATCH;
    }
}
