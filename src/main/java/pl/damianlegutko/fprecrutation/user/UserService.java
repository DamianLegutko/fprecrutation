package pl.damianlegutko.fprecrutation.user;

import pl.damianlegutko.fprecrutation.user.api.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public interface UserService {
    UserDTO findUserByUsername(@NotBlank @Size(min = 3, max = 40) String username);
    void saveUser(UserDTO user);
    void giveMoneyToUser(@Size(min = 3, max = 40) String userName,
                         @NotNull @Positive BigDecimal moneyAmount);
    void takeMoneyFromUser(@Size(min = 3, max = 40) String userName,
                           @NotNull @Positive BigDecimal moneyAmount);
}
