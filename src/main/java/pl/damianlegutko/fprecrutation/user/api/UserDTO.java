package pl.damianlegutko.fprecrutation.user.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.Validators;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.user.exceptions.PasswordNotMatchException;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;

    private String password;
    private String confirmPassword;

    private BigDecimal money;

    public void validateAllFields() throws EmptyFieldException, PasswordNotMatchException {
        Validators.objectIsNotNull(username, "username");
        Validators.objectIsNotNull(password, "password");
        Validators.objectIsNotNull(confirmPassword, "confirmPassword");
        Validators.objectIsNotNull(money, "money");
        Validators.equalsPassword(password, confirmPassword);
    }
}
