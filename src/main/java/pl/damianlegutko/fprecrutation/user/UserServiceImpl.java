package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import java.math.BigDecimal;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service("serviceForUsers")
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @SneakyThrows
    public UserDTO findUserByUsername(String userName) {
        User user = userRepository.findByUsername(userName);

        if (isNull(user)) throw new UserNotExistsException(userName);

        return mapUserToDto(user);
    }

    @SneakyThrows
    public void saveUser(UserDTO user) {
        if (nonNull(userRepository.findByUsername(user.getUsername()))) throw new UserAlreadyExistsException(user.getUsername());

        userRepository.save(mapDtoToUser(user));
    }

    @SneakyThrows
    public void giveMoneyToUser(String userName, BigDecimal moneyAmount) {
        User user = userRepository.findByUsername(userName);

        if (isNull(user)) throw new UserNotExistsException(userName);

        user.setMoney(user.getMoney().add(moneyAmount));
        userRepository.save(user);
    }

    @SneakyThrows
    public void takeMoneyFromUser(String userName, BigDecimal moneyAmount) {
        User user = userRepository.findByUsername(userName);

        if (isNull(user)) throw new UserNotExistsException(userName);

        if (user.getMoney().compareTo(moneyAmount) < 0) throw new UserHaveNotEnoughMoneyException(userName, user.getMoney());

        user.setMoney(user.getMoney().subtract(moneyAmount));
        userRepository.save(user);
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
