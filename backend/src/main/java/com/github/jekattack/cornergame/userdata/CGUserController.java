package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.userdata.cgUserDetails.Visit;
import com.github.jekattack.cornergame.userdata.cgUserDetails.VisitCreationData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @PostMapping("/visit/add")
    public ResponseEntity<HttpStatus> addVisit(@RequestBody VisitCreationData visitCreationData, Principal principal) {
        try {
            cgUserService.addVisit(visitCreationData, principal.getName());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/progress")
    public List<Visit> getUsersVisits(Principal principal){
        return cgUserService.findByUsername(principal.getName()).orElseThrow().getVisits();
    }

}
