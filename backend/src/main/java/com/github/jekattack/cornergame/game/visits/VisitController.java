package com.github.jekattack.cornergame.game.visits;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void createVisit(@RequestBody VisitCreationData visitCreationData, Principal principal) {
        try {
            //principal.getName() contains userId
            visitService.createVisit(visitCreationData, principal.getName());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Visit not created: To far away or already visited within 24h");
        }
    }

    @GetMapping("/progress")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<Visit> getUsersVisits(Principal principal){
        try{
            //principal.getName() contains userId
            return visitService.getUsersVisits(principal.getName());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No visits found for UserId:" + principal.getName());
        }

    }

}
