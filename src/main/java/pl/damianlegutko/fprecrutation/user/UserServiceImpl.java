package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import static java.util.Objects.nonNull;

@Service("user2service")
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @SneakyThrows
    public UserDTO findUserByUsername(String userName) {
        User user = userRepository.findByUsername(userName);
        if (nonNull(user)) {
            return mapUserToDto(user);
        }
        throw new UserNotExistsException();
    }

    @SneakyThrows
    public void saveUser(UserDTO user) {
        if(nonNull(userRepository.findByUsername(user.getUsername()))) throw new UserAlreadyExistsException();

        userRepository.save(mapDtoToUser(user));
    }

    private User mapDtoToUser(UserDTO user) {
        return User.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    private UserDTO mapUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .build();
    }
}
