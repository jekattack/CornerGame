package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserService {

    private final CGUserRepository cgUserRepository;
    private final CGUserGameDataService cgUserGameDataService;
    private final PasswordEncoder passwordEncoder;

    public CGUserDTO createUser(UserCreationData userCreationData) {
        if(userCreationData.getUsername()==null || userCreationData.getUsername().isBlank()) throw new IllegalArgumentException("Registration failed: No username set");
        if(userCreationData.getEmail()==null || userCreationData.getEmail().isBlank()) throw new IllegalArgumentException("Registration failed: No email set");
        if(userCreationData.getPassword()==null || userCreationData.getPassword().isBlank()) throw new IllegalArgumentException("Registration failed: No password set");
        if(!(userCreationData.getPassword().equals(userCreationData.getPasswordAgain()))) throw new IllegalArgumentException("Password validation failed: Entered passwords don't match");

        CGUser cgUser = new CGUser(userCreationData.getUsername().toLowerCase(), userCreationData.getEmail(), userCreationData.getPassword());
        cgUser.setPassword(passwordEncoder.encode(cgUser.getPassword()));
        cgUser.setRole("user");
        CGUser newUser = cgUserRepository.save(cgUser);
        cgUserGameDataService.createGameData(newUser.getId());
        return new CGUserDTO("User " + newUser.getUsername() + " created!", newUser.getUsername());
    }


    public CGUser findByUsername(String username) {
        Optional<CGUser> user = cgUserRepository.findByUsername(username.toLowerCase());
        if(user.isPresent()){
            return user.get();
        } else {
            throw new NoSuchElementException("User not found for username.");
        }
    }

    public Optional<CGUser> findById(String id) {
        return cgUserRepository.findById(id);
    }


    public CGUser getUser(String userId) {
        Optional<CGUser> user = cgUserRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new NoSuchElementException("User not found for Id " + userId);
        }
    }

    public CGUser updateUser(String userId, CGUserUpdateDTO updateDTO) {
        Optional<CGUser> userOptional = cgUserRepository.findById(userId);
        if(userOptional.isPresent()){
            CGUser user = userOptional.get();
            if(updateDTO.getFirstname()!=null){
                user.setFirstname(updateDTO.getFirstname());
            }
            if(updateDTO.getLastname()!=null){
                user.setLastname(updateDTO.getLastname());
            }
            if(updateDTO.getPhone()!=null){
                user.setPhone(updateDTO.getPhone());
            }
            if(updateDTO.getStammkioskId()!=null){
                user.setStammkioskId(updateDTO.getStammkioskId());
            }
            return cgUserRepository.save(user);
        } else {
            throw new NoSuchElementException("User not found for Id " + userId);
        }
    }

    public String updatePassword(String userId, CGUserPasswordDTO updateDTO) {
        if(updateDTO.getPassword()==null || updateDTO.getPassword().isBlank()) throw new IllegalArgumentException("Registration failed: No password set");
        if(!(updateDTO.getPassword().equals(updateDTO.getPasswordAgain()))) throw new IllegalArgumentException("Password validation failed: Entered passwords don't match");

        CGUser cgUser = cgUserRepository.findById(userId).orElseThrow();
        cgUser.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        cgUser.setRole("user");
        CGUser newUser = cgUserRepository.save(cgUser);
        cgUserGameDataService.createGameData(newUser.getId());
        return "Password erfolgreich geändert!";
    }
}
