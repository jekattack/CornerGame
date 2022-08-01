package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.model.CGErrorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Object> createVisit(@RequestBody VisitCreationData visitCreationData, Principal principal) {
        try {
            //principal.getName() contains userId
            return ResponseEntity.status(HttpStatus.CREATED).body(visitService.createVisit(visitCreationData, principal.getName()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Visit not created", e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @GetMapping("/progress")
    public ResponseEntity<Object> getUsersVisits(Principal principal){
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(visitService.getUsersVisits(principal.getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("No Visits found", e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

}
