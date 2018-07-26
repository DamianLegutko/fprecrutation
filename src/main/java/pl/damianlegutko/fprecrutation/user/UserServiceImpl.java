package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
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

    @SneakyThrows
    public void giveMoneyToUser(String userName, double moneyAmount) {
        User user = userRepository.findByUsername(userName);

        if (nonNull(user)) {
            user.setMoney(DoubleOperations.add(user.getMoney(), moneyAmount));

            userRepository.save(user);
        }
        else throw new UserNotExistsException();
    }

    @SneakyThrows
    public void takeMoneyFromUser(String userName, double moneyAmount) {
        User user = userRepository.findByUsername(userName);

        if (nonNull(user)) {
            if (DoubleOperations.compare(user.getMoney(), moneyAmount) < 0) throw new UserHaveNotEnoughMoneyException();

            user.setMoney(DoubleOperations.subtract(user.getMoney(), moneyAmount));

            userRepository.save(user);
        }
        else throw new UserNotExistsException();
    }

    private User mapDtoToUser(UserDTO user) {
        return User.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .money(user.getMoney())
                .build();
    }

    private UserDTO mapUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .money(user.getMoney())
                .build();
    }
}
