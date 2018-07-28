package pl.damianlegutko.fprecrutation.commonExceptions;

import pl.damianlegutko.fprecrutation.Messages;

public class EmptyFieldException extends BaseException {

    public EmptyFieldException(String fieldName) {
        super(Messages.get("validation.field.notEmpty", fieldName));
        this.errorCode = ErrorCode.EMPTY_FIELD;
    }
}