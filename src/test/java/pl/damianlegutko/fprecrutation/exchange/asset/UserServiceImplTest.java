package pl.damianlegutko.fprecrutation.exchange.asset;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionSystemException;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.configuration.AppTestConfig;
import pl.damianlegutko.fprecrutation.user.SecurityService;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;
import pl.damianlegutko.fprecrutation.user.exceptions.PasswordNotMatchException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    private static final String USER_NAME = "USER";
    private static final String USER_PASSWORD = "QWE123zxc";
    private static final BigDecimal START_WALLET = new BigDecimal(1000000);
    private String testUserName = null;

    @Before
    public void setUp() {
        testUserName = USER_NAME + RandomStringUtils.random(10, true, false);
    }

    @After
    public void closeUp() {
        testUserName = null;
    }

    //region Sanity tests
    @Test
    public void registration_withSuccess_whenUserNotExists() {
        //given
        UserDTO userDTO = UserDTO.builder()
                            .username(testUserName.toLowerCase())
                            .password(USER_PASSWORD)
                            .confirmPassword(USER_PASSWORD)
                            .money(START_WALLET)
                            .build();

        //when
        userService.saveUser(userDTO);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        assertThat(receivedUser.getUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void registration_withSuccess_whenUserNotExists_and_userNameHaveBigLetters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                            .username(testUserName)
                            .password(USER_PASSWORD)
                            .confirmPassword(USER_PASSWORD)
                            .money(START_WALLET)
                            .build();

        //when
        userService.saveUser(userDTO);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        assertThat(receivedUser.getUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void signIn_withSuccess_whenUserExists() {
        //given
        UserDTO userDTO = UserDTO.builder()
                            .username(testUserName.toLowerCase())
                            .password(USER_PASSWORD)
                            .confirmPassword(USER_PASSWORD)
                            .money(START_WALLET)
                            .build();

        //when
        userService.saveUser(userDTO);

        userService.signin(userDTO.getUsername(), userDTO.getPassword());

        //than
        assertThat(securityService.findLoggedInUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void signIn_withSuccess_whenUserExists_and_userNameHaveBigLetters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                            .username(testUserName)
                            .password(USER_PASSWORD)
                            .confirmPassword(USER_PASSWORD)
                            .money(START_WALLET)
                            .build();

        //when
        userService.saveUser(userDTO);

        userService.signin(userDTO.getUsername(), userDTO.getPassword());

        //than
        assertThat(securityService.findLoggedInUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void signIn_withSuccess_twiceTime() {
        //given
        UserDTO userDTO = UserDTO.builder()
                            .username(testUserName.toLowerCase())
                            .password(USER_PASSWORD)
                            .confirmPassword(USER_PASSWORD)
                            .money(START_WALLET)
                            .build();

        userService.saveUser(userDTO);

        //when
        userService.signin(userDTO.getUsername(), userDTO.getPassword());
        userService.signin(userDTO.getUsername(), userDTO.getPassword());

        //than
        assertThat(securityService.findLoggedInUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void findUserByUsername_withSuccess_whenUserExists() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName.toLowerCase())
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        userService.saveUser(userDTO);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        assertThat(receivedUser.getUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void findUserByUsername_withSuccess_whenUserExists_and_userNameHaveBigLetters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        userService.saveUser(userDTO);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        assertThat(receivedUser.getUsername()).isEqualTo(testUserName.toLowerCase());
    }

    @Test
    public void giveMoneyToUser_withSuccess_allCorrect() {
        //given
        BigDecimal moneyToGive = new BigDecimal(11.11);

        UserDTO userDTO = UserDTO.builder()
                .username(testUserName.toLowerCase())
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        userService.saveUser(userDTO);

        //when
        userService.giveMoneyToUser(userDTO.getUsername(), moneyToGive);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        BigDecimal currentValue = receivedUser.getMoney().setScale(2);
        BigDecimal expectedValue = userDTO.getMoney().add(moneyToGive).setScale(2, BigDecimal.ROUND_HALF_UP);

        assertThat(currentValue).isEqualTo(expectedValue);
    }

    @Test
    public void giveMoneyToUser_withSuccess_allCorrect_and_userNameHaveBigLetters() {
        //given
        BigDecimal moneyToGive = new BigDecimal(11.11);

        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        userService.saveUser(userDTO);

        //when
        userService.giveMoneyToUser(userDTO.getUsername(), moneyToGive);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        BigDecimal currentValue = receivedUser.getMoney().setScale(2);
        BigDecimal expectedValue = userDTO.getMoney().add(moneyToGive).setScale(2, BigDecimal.ROUND_HALF_UP);

        assertThat(currentValue).isEqualTo(expectedValue);
    }

    @Test
    public void takeMoneyFromUser_withSuccess_allCorrect() {
        //given
        BigDecimal moneyToGive = new BigDecimal(11.11);

        UserDTO userDTO = UserDTO.builder()
                .username(testUserName.toLowerCase())
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        userService.saveUser(userDTO);

        //when
        userService.takeMoneyFromUser(userDTO.getUsername(), moneyToGive);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        BigDecimal currentValue = receivedUser.getMoney().setScale(2);
        BigDecimal expectedValue = userDTO.getMoney().subtract(moneyToGive).setScale(2, BigDecimal.ROUND_HALF_UP);

        assertThat(currentValue).isEqualTo(expectedValue);
    }

    @Test
    public void takeMoneyFromUser_withSuccess_allAreCorrect_and_userNameHaveBigLetters() {
        //given
        BigDecimal moneyToGive = new BigDecimal(11.11);

        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        userService.saveUser(userDTO);

        //when
        userService.takeMoneyFromUser(userDTO.getUsername(), moneyToGive);

        //than
        UserDTO receivedUser = userService.findUserByUsername(userDTO.getUsername());

        BigDecimal currentValue = receivedUser.getMoney().setScale(2);
        BigDecimal expectedValue = userDTO.getMoney().subtract(moneyToGive).setScale(2, BigDecimal.ROUND_HALF_UP);

        assertThat(currentValue).isEqualTo(expectedValue);
    }
    //endregion

    //region Validation tests
    @Test
    public void findUserByUsername_throwEmptyFieldException_whenUserNameIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(null)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.findUserByUsername(userDTO.getUsername());
            fail("Fail because searching require [userName].");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessageContaining("must not be blank");
        }
    }

    @Test
    public void findUserByUsername_throwConstraintViolationException_whenUserNameHaveBelow3Letters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username("ab")
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.findUserByUsername(userDTO.getUsername());
            fail("Fail because searching require [userName] have at least 3 letters.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessageContaining("size must be between 3 and 40");
        }
    }

    @Test
    public void findUserByUsername_throwConstraintViolationException_whenUserNameHaveAbove40Letters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username("abcdefghi0abcdefghi1abcdefghi3abcdefghi4x")
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.findUserByUsername(userDTO.getUsername());
            fail("Fail because searching require [userName] max 40 letters.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessageContaining("size must be between 3 and 40");
        }
    }

    @Test
    public void registration_throwEmptyFieldException_whenUserNameIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(null)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [userName].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void registration_throwEmptyFieldException_whenPasswordIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(null)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [password].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void registration_throwEmptyFieldException_whenConfirmPasswordIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(null)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [confirmPassword].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void registration_throwEmptyFieldException_whenMoneyIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(null)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [money].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void registration_throwConstraintViolationException_whenUserNameHaveBelow3Letters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username("ab")
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [userName] have at least 3 letters.");
        }
        catch (TransactionSystemException exception) {
            //than
            assertThat(exception.getCause().getCause()).isInstanceOf(ConstraintViolationException.class)
                                                       .hasMessageContaining("size must be between 3 and 40");
        }
    }

    @Test
    public void registration_throwConstraintViolationException_whenUserNameHaveAbove40Letters() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username("abcdefghi0abcdefghi1abcdefghi3abcdefghi4x")
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [userName] max 40 letters.");
        }
        catch (TransactionSystemException exception) {
            //than
            assertThat(exception.getCause().getCause()).isInstanceOf(ConstraintViolationException.class)
                                                       .hasMessageContaining("size must be between 3 and 40");
        }
    }

    @Test
    public void signin_throwEmptyFieldException_whenUserNameIsNull() {
        //given
        //when
        try {
            userService.signin(null, USER_PASSWORD);
            fail("Fail because signin require [userName].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void signin_throwEmptyFieldException_whenPasswordIsNull() {
        //given
        //when
        try {
            userService.signin(testUserName, null);
            fail("Fail because signin require [password].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getUndeclaredThrowable()).isInstanceOf(EmptyFieldException.class);
        }
    }

    @Test
    public void giveMoneyToUser_throwEmptyFieldException_whenMoneyIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(null)
                .build();

        //when
        try {
            userService.giveMoneyToUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require [money].");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessageContaining("must not be null");
        }
    }

    @Test
    public void giveMoneyToUser_throwConstraintViolationException_whenMoneyAmountIsBelow0() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(new BigDecimal(-1))
                .build();

        //when
        try {
            userService.giveMoneyToUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require [money] must be greater than or equal to 0.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("must be greater than or equal to 0");
        }
    }

    @Test
    public void giveMoneyToUser_throwConstraintViolationException_whenMoneyAmountIsAbove999999999() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(new BigDecimal(1000000000))
                .build();

        //when
        try {
            userService.giveMoneyToUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require [money] must be less than or equal to 999999999.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("must be less than or equal to 999999999");
        }
    }

    @Test
    public void takeMoneyFromUser_throwEmptyFieldException_whenMoneyIsNull() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(null)
                .build();

        //when
        try {
            userService.takeMoneyFromUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require [money].");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                                 .hasMessageContaining("must not be null");
        }
    }

    @Test
    public void takeMoneyFromUser_throwConstraintViolationException_whenMoneyAmountIsBelow0() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(new BigDecimal(-1))
                .build();

        //when
        try {
            userService.takeMoneyFromUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require [money] must be greater than or equal to 0.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("must be greater than or equal to 0");
        }
    }

    @Test
    public void takeMoneyFromUser_throwConstraintViolationException_whenMoneyAmountIsAbove999999999() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(new BigDecimal(1000000000))
                .build();

        //when
        try {
            userService.takeMoneyFromUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require [money] must be less than or equal to 999999999.");
        }
        catch (ConstraintViolationException exception) {
            //than
            assertThat(exception).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("must be less than or equal to 999999999");
        }
    }
    //endregion

    //region Other cases

    @Test
    public void registration_withoutSuccess_whenConfirmPasswordAreDifferentThanPassword() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName.toLowerCase())
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD + "a")
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.saveUser(userDTO);
            fail("Fail because registration require [password] equals [confirmPassword].");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getCause()).isInstanceOf(PasswordNotMatchException.class);
        }
    }

    @Test
    public void giveMoneyToUser_throwUserNotExistsException_whenUserNotExists() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.giveMoneyToUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because giveMoneyToUser require user to exists.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getCause()).isInstanceOf(UserNotExistsException.class);
        }
    }

    @Test
    public void takeMoneyFromUser_throwUserNotExistsException_whenUserNotExists() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        //when
        try {
            userService.takeMoneyFromUser(userDTO.getUsername(), userDTO.getMoney());
            fail("Fail because takeMoneyFromUser require user to exists.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getCause()).isInstanceOf(UserNotExistsException.class);
        }
    }

    @Test
    public void takeMoneyFromUser_throwUserHaveNotEnoughMoneyException_whenUserHaveNotEnoughMoney() {
        //given
        UserDTO userDTO = UserDTO.builder()
                .username(testUserName)
                .password(USER_PASSWORD)
                .confirmPassword(USER_PASSWORD)
                .money(START_WALLET)
                .build();

        userService.saveUser(userDTO);

        //when
        try {
            userService.takeMoneyFromUser(userDTO.getUsername(), userDTO.getMoney().add(new BigDecimal(1)));
            fail("Fail because takeMoneyFromUser require user have enough money in wallet.");
        }
        catch (UndeclaredThrowableException exception) {
            //than
            assertThat(exception.getCause()).isInstanceOf(UserHaveNotEnoughMoneyException.class);
        }
    }
    //endregion
}