package lohika.club.jfr.service;

import lohika.club.jfr.model.User;
import lohika.club.jfr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(cacheNames = "users")
    public Optional<User> findUserByUsername(String username) {
        log.info("Find user by username [username={}]", username);
        return userRepository.findByUsername(username);
    }
}
