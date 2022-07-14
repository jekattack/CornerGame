package com.github.jekattack.cornergame.userdata;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CGUserController {

    private final CGUserService cgUserService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestBody UserCreationData userCreationData){
        try {
            cgUserService.createUser(userCreationData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e1) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e2) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public String getUsername(Principal principal) {
        return principal.getName();
    }


}
