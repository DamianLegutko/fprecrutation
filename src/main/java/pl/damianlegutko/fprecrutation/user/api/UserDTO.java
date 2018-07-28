package pl.damianlegutko.fprecrutation.user.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.Validators;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;

    private String password;

    private BigDecimal money;

    public void validateAllFields() throws EmptyFieldException {
        Validators.objectIsNotNull(username, "username");
        Validators.objectIsNotNull(password, "password");
        Validators.objectIsNotNull(money, "money");
    }
}
