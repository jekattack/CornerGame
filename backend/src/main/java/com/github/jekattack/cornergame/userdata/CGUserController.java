package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.model.CGErrorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CGUserController {

    private final CGUserService cgUserService;

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody UserCreationData userCreationData){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(cgUserService.createUser(userCreationData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("User not created", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getUser(Principal principal) {
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(cgUserService.getUser(principal.getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("User not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }

    }

    @PostMapping
    @RequestMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody CGUserUpdateDTO updateDTO, Principal principal) {
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(cgUserService.updateUser(principal.getName(), updateDTO));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @PostMapping
    @RequestMapping("/update/password")
    public ResponseEntity<Object> updatePassword(@RequestBody CGUserPasswordDTO updateDTO, Principal principal){
        try {
            return ResponseEntity.ok().body(cgUserService.updatePassword(principal.getName(), updateDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Password not changed", e.getMessage()));
        }
    }



}
