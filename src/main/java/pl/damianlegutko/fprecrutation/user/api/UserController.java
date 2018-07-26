package pl.damianlegutko.fprecrutation.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.user.UserServiceImpl;
import pl.damianlegutko.fprecrutation.user.exceptions.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl user2service;

    @GetMapping("/get/{login}")
    ResponseEntity getUser(@PathVariable String login) {
       UserDTO userDTO = user2service.findUserByUsername(login);
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
        user2service.findUserByUsername(user.getUsername());//TODO dodac logowanie
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotExistsException.class)
    void userNotFound() {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    void userExists() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    void unhandledUserError() {}
}
