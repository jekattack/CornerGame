package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.game.Visit;
import com.github.jekattack.cornergame.game.VisitCreationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserService {

    private final CGUserRepository cgUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserCreationData userCreationData) {
        if(userCreationData.getUsername()==null || userCreationData.getUsername().isBlank()) throw new IllegalArgumentException("Registration failed: No username set");
        if(userCreationData.getEmail()==null || userCreationData.getEmail().isBlank()) throw new IllegalArgumentException("Registration failed: No email set");
        if(userCreationData.getPassword()==null || userCreationData.getPassword().isBlank()) throw new IllegalArgumentException("Registration failed: No password set");
        if(!(userCreationData.getPassword().equals(userCreationData.getPasswordAgain()))) throw new IllegalArgumentException("Password validation failed: Entered passwords don't match");

        CGUser cgUser = new CGUser(userCreationData.getUsername().toLowerCase(), userCreationData.getEmail(), userCreationData.getPassword());
        cgUser.setPassword(passwordEncoder.encode(cgUser.getPassword()));
        cgUser.setRole("user");
        cgUserRepository.save(cgUser);
    }


    public Optional<CGUser> findByUsername(String username) {
        return cgUserRepository.findByUsername(username.toLowerCase());
    }
    public Optional<CGUser> findById(String id) {
        return cgUserRepository.findById(id);
    }


}
