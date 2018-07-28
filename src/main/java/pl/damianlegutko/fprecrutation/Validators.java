package pl.damianlegutko.fprecrutation;

import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;

public class Validators {
    public static void objectIsNotNull(Object object, String parameterName) throws EmptyFieldException {
        if (object == null) throw new EmptyFieldException(parameterName);
    }
}