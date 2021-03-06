package pl.damianlegutko.fprecrutation.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.responses.ExceptionMessage;
import pl.damianlegutko.fprecrutation.responses.ValidationMessage;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.exceptions.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @GetMapping("/get/{userName}")
    ResponseEntity getUser(@PathVariable String userName) throws UserNotExistsException {
       UserDTO userDTO = userService.findUserByUsername(userName);
       return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    void registration(@RequestBody UserDTO user) {
        userService.saveUser(user);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void signin(@RequestBody UserDTO user) {
        userService.signin(user.getUsername(), user.getPassword());
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
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

    @ExceptionHandler(PasswordNotMatchException.class)
    ResponseEntity unhandledPasswordNotMatch(PasswordNotMatchException exception) {
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
