package lohika.club.jfr.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordManager {

    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    public String createHash(String password) {
        return passwordEncoder.encode(password);
    }

}
