package pl.damianlegutko.fprecrutation.user;

import pl.damianlegutko.fprecrutation.user.api.UserDTO;

import java.math.BigDecimal;

public interface UserService {
    UserDTO findUserByUsername(String username);
    void saveUser(UserDTO user);
    void giveMoneyToUser(String userName, BigDecimal moneyAmount);
    void takeMoneyFromUser(String userName, BigDecimal moneyAmount);
}
