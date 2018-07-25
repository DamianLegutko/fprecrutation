package pl.damianlegutko.fprecrutation.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.user.UserAlreadyExistsException;
import pl.damianlegutko.fprecrutation.user.UserNotExistsException;
import pl.damianlegutko.fprecrutation.user.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
@Slf4j
class UserController {

    @Autowired
    private UserServiceImpl user2service;

    @GetMapping("/get/{login}")
    ResponseEntity getUser(@PathVariable String login) {
       UserDTO userDTO = user2service.findUserByUsername(login);
       return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/registration")
    void registration(@RequestBody UserDTO user) {
        user2service.saveUser(user);
    }

    @PostMapping(value = "/signin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void signin(@RequestBody UserDTO user) {
        user2service.findUserByUsername(user.getUsername());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotExistsException.class)
    void userNotFound() {
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    void userExists() {
    }
}
