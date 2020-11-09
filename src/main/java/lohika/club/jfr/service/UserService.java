package lohika.club.jfr.service;

import lohika.club.jfr.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUsername(String username);

}
