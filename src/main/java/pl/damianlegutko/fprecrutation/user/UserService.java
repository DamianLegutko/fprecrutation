package pl.damianlegutko.fprecrutation.user;

import pl.damianlegutko.fprecrutation.user.api.UserDTO;

public interface UserService {

    UserDTO findUserByUsername(String username);
    void saveUser(UserDTO user);
}
