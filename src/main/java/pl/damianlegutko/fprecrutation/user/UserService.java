package pl.damianlegutko.fprecrutation.user;

import pl.damianlegutko.fprecrutation.user.api.UserDTO;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public interface UserService {
    UserDTO findUserByUsername(@NotBlank @Size(min = 3, max = 40) String username);
    void saveUser(UserDTO user);
    void giveMoneyToUser(@Size(min = 3, max = 40) String userName,
                         @NotNull @Min(0) @Max(999999999) BigDecimal moneyAmount);
    void takeMoneyFromUser(@Size(min = 3, max = 40) String userName,
                           @NotNull @NotNull @Min(0) @Max(999999999) BigDecimal moneyAmount);
}
