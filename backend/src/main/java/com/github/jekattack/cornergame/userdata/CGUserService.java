package com.github.jekattack.cornergame.userdata;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CGUserService {

    private final CGUserRepository cgUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserCreationData userCreationData) {
        if(userCreationData.getUsername()==null || userCreationData.getUsername().isBlank()) throw new IllegalArgumentException("Registration failed: No username set");
        if(userCreationData.getEmail()==null || userCreationData.getEmail().isBlank()) throw new IllegalArgumentException("Registration failed: No email set");
        if(userCreationData.getPassword()==null || userCreationData.getPassword().isBlank()) throw new IllegalArgumentException("Registration failed: No password set");
        if(!(userCreationData.getPassword().equals(userCreationData.getPasswordAgain()))) throw new IllegalArgumentException("Password validation failed: repeated password doesn't match");

        CGUser cgUser = new CGUser(userCreationData.getUsername(), userCreationData.getEmail(), userCreationData.getPassword());
        cgUser.setPassword(passwordEncoder.encode(cgUser.getPassword()));
        cgUser.setRole("user");
        cgUserRepository.save(cgUser);
    }


    public Optional<CGUser> findByUsername(String username) {
        return cgUserRepository.findByUsername(username);
    }
    public Optional<CGUser> findById(String id) {
        return cgUserRepository.findById(id);
    }
    public Optional<CGUser> findByEmail(String email) {
        return cgUserRepository.findByEmail(email);
    }
}
