package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.damianlegutko.fprecrutation.Validators;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.PasswordNotMatchException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
@Validated
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @SneakyThrows
    public UserDTO findUserByUsername(String userName) {
        Validators.objectIsNotNull(userName, "userName");

        User user = userRepository.findByUsername(userName.toLowerCase());

        if (isNull(user)) throw new UserNotExistsException(userName.toLowerCase());

        return mapUserToDto(user);
    }

    @SneakyThrows
    public void saveUser(UserDTO user) {
        user.validateAllFields();

        if (nonNull(userRepository.findByUsername(user.getUsername().toLowerCase()))) throw new UserAlreadyExistsException(user.getUsername());

        userRepository.save(mapDtoToUser(user));
    }

    @SneakyThrows
    public void giveMoneyToUser(String userName, BigDecimal moneyAmount) {
        Validators.objectIsNotNull(userName, "userName");
        Validators.objectIsNotNull(moneyAmount, "moneyAmount");

        User user = userRepository.findByUsername(userName.toLowerCase());

        if (isNull(user)) throw new UserNotExistsException(userName);

        user.setMoney(user.getMoney().add(moneyAmount));
        userRepository.save(user);
    }

    @SneakyThrows
    public void takeMoneyFromUser(String userName, BigDecimal moneyAmount) {
        Validators.objectIsNotNull(userName, "userName");
        Validators.objectIsNotNull(moneyAmount, "moneyAmount");

        User user = userRepository.findByUsername(userName.toLowerCase());

        if (isNull(user)) throw new UserNotExistsException(userName);

        if (user.getMoney().compareTo(moneyAmount) < 0) throw new UserHaveNotEnoughMoneyException(userName, user.getMoney());

        user.setMoney(user.getMoney().subtract(moneyAmount));
        userRepository.save(user);
    }

    @SneakyThrows
    public void signin(String userName, String password) {
        Validators.objectIsNotNull(userName, "userName");
        Validators.objectIsNotNull(password, "password");

        securityService.signin(userName.toLowerCase(), password);
    }

    @SneakyThrows
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        securityService.logout(request, response);
    }

    private User mapDtoToUser(UserDTO user) throws EmptyFieldException, PasswordNotMatchException {
        user.validateAllFields();

        return User.builder()
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .username(user.getUsername().toLowerCase())
                .money(user.getMoney())
                .roles(newHashSet(roleRepository.findAll()))
                .build();
    }

    private UserDTO mapUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername().toLowerCase())
                .money(user.getMoney())
                .build();
    }
}
