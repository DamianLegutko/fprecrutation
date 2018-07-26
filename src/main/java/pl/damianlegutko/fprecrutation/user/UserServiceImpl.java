package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import java.math.BigDecimal;

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
    public void giveMoneyToUser(String userName, BigDecimal moneyAmount) {
        User user = userRepository.findByUsername(userName);

        if (nonNull(user)) {
            user.setMoney(user.getMoney().add(moneyAmount));

            userRepository.save(user);
        }
        else throw new UserNotExistsException();
    }

    @SneakyThrows
    public void takeMoneyFromUser(String userName, BigDecimal moneyAmount) {
        User user = userRepository.findByUsername(userName);

        if (nonNull(user)) {
            if (user.getMoney().compareTo(moneyAmount) < 0) throw new UserHaveNotEnoughMoneyException();

            user.setMoney(user.getMoney().subtract(moneyAmount));

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
