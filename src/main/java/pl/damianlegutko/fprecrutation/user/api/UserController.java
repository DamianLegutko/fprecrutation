package pl.damianlegutko.fprecrutation.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.responses.ExceptionMessage;
import pl.damianlegutko.fprecrutation.responses.ValidationMessage;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import javax.validation.ValidationException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @GetMapping("/get/{userName}")
    ResponseEntity getUser(@PathVariable String userName) {
       UserDTO userDTO = userService.findUserByUsername(userName);
       return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    void registration(@RequestBody UserDTO user) {
        userService.saveUser(user);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    void signin(@RequestBody UserDTO user) {
        userService.findUserByUsername(user.getUsername());
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    void signin() {
        //TODO
    }

    //TODO temporary REST
    @PostMapping("/addCash/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void increaseUserCash(@PathVariable String userName, @RequestParam BigDecimal amount) {
        userService.giveMoneyToUser(userName, amount);
    }

    //TODO temporary REST
    @PostMapping("/subtractCash/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void decreaseUserCash(@PathVariable String userName, @RequestParam BigDecimal amount) {
        userService.takeMoneyFromUser(userName, amount);
    }

    @ExceptionHandler(UserNotExistsException.class)
    ResponseEntity userNotFound(UserNotExistsException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity userExists(UserAlreadyExistsException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserHaveNotEnoughMoneyException.class)
    ResponseEntity userHaveNotEnaughMoney(UserHaveNotEnoughMoneyException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyFieldException.class)
    ResponseEntity stockHaveNotEnoughStocks(EmptyFieldException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    ResponseEntity unhandledUserError(UserException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity handleValidation(ValidationException exception) {
        return ValidationMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }
}
