package pl.damianlegutko.fprecrutation.user;

import pl.damianlegutko.fprecrutation.user.api.UserDTO;

interface UserService {
    UserDTO findUserByUsername(String username);
    void saveUser(UserDTO user);
    void giveMoneyToUser(String userName, double moneyAmount);
    void takeMoneyFromUser(String userName, double moneyAmount);
}
