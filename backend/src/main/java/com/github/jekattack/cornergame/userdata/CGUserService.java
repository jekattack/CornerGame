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
        if(userCreationData.getUsername()==null || userCreationData.getUsername().isBlank()) throw new IllegalArgumentException("Du brauchst einen Usernamen. 👀");
        if(userCreationData.getEmail()==null || userCreationData.getEmail().isBlank()) throw new IllegalArgumentException("Bitte gib deine Mail-Adresse ein.");
        if(userCreationData.getPassword()==null || userCreationData.getPassword().isBlank()) throw new IllegalArgumentException("Du brauchst ein Password. 👀");
        if(!(userCreationData.getPassword().equals(userCreationData.getPasswordAgain()))) throw new IllegalArgumentException("Passwörter unterschiedlich. 😵‍💫");

        CGUser cgUser = new CGUser(userCreationData.getUsername().toLowerCase(), userCreationData.getEmail(), userCreationData.getPassword());
        cgUser.setPassword(passwordEncoder.encode(cgUser.getPassword()));
        cgUser.setRole("user");
        CGUser newUser = cgUserRepository.save(cgUser);
        cgUserGameDataService.createGameData(newUser.getId());
        return new CGUserDTO(newUser.getUsername() + " angelegt!", newUser.getUsername());
    }


    public CGUser findByUsername(String username) {
        Optional<CGUser> user = cgUserRepository.findByUsername(username.toLowerCase());
        if(user.isPresent()){
            return user.get();
        } else {
            throw new NoSuchElementException("Kein Accout für " + username + ". 🔭");
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
            throw new NoSuchElementException("Für " + userId + " kein Nutzer gefunden. 🔎");
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
            throw new NoSuchElementException("Für " + userId + " kein Nutzer gefunden. 🔎");
        }
    }

    public String updatePassword(String userId, CGUserPasswordDTO updateDTO) {
        if(updateDTO.getPassword()==null || updateDTO.getPassword().isBlank()) throw new IllegalArgumentException("Passwort darf nicht leer sein… 🕳");
        if(!(updateDTO.getPassword().equals(updateDTO.getPasswordAgain()))) throw new IllegalArgumentException("Passwörter nicht gleich. 🤼");

        CGUser cgUser = cgUserRepository.findById(userId).orElseThrow();
        cgUser.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        cgUser.setRole("user");
        CGUser newUser = cgUserRepository.save(cgUser);
        cgUserGameDataService.createGameData(newUser.getId());
        return "Password geändert! 👌";
    }
}
