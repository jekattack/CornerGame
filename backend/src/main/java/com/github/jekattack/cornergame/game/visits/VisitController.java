package com.github.jekattack.cornergame.game.visits;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> createVisit(@RequestBody VisitCreationData visitCreationData, Principal principal) {
        try {
            visitService.createVisit(visitCreationData, principal.getName());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/progress")
    public List<Visit> getUsersVisits(Principal principal){
        return visitService.getUsersVisits(principal.getName());
    }
}
