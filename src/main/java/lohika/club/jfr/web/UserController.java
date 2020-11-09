package lohika.club.jfr.web;

import lohika.club.jfr.model.User;
import lohika.club.jfr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/users")
    public Optional<User> getUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }

}
