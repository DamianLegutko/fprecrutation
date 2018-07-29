package pl.damianlegutko.fprecrutation;

import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.user.exceptions.PasswordNotMatchException;

public class Validators {
    public static void objectIsNotNull(Object object, String parameterName) throws EmptyFieldException {
        if (object == null) throw new EmptyFieldException(parameterName);
    }

    public static void equalsPassword(String password, String confirmPassword) throws PasswordNotMatchException {
        if (!password.equals(confirmPassword)) throw new PasswordNotMatchException();
    }
}