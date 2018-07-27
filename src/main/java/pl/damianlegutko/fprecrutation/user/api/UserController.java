package pl.damianlegutko.fprecrutation.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
class UserController {

    private final UserService user2service;

    @GetMapping("/get/{userName}")
    ResponseEntity getUser(@PathVariable String userName) {
       UserDTO userDTO = user2service.findUserByUsername(userName);
       return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    void registration(@RequestBody UserDTO user) {
        user2service.saveUser(user);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void signin(@RequestBody UserDTO user) {
        user2service.findUserByUsername(user.getUsername());
    }

    //TODO temporary REST
    @PostMapping("/addCash/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void increaseUserCash(@PathVariable String userName, @RequestParam BigDecimal amount) {
        user2service.giveMoneyToUser(userName, amount);
    }

    //TODO temporary REST
    @PostMapping("/subtractCash/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void decreaseUserCash(@PathVariable String userName, @RequestParam BigDecimal amount) {
        user2service.takeMoneyFromUser(userName, amount);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotExistsException.class)
    void userNotFound() {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    void userExists() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserHaveNotEnoughMoneyException.class)
    void userHaveNotEnaughMoney() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    void unhandledUserError() {}
}
