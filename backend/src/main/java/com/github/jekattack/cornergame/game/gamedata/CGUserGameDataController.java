package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.gamedata.achievements.Achievement;
import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
import com.github.jekattack.cornergame.model.CGErrorDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/gamedata")
@RequiredArgsConstructor
public class CGUserGameDataController {

    private final CGUserGameDataService cgUserGameDataService;

    @GetMapping("/highscore")
    public ResponseEntity<Object> getTop10Highscore(){
        try {
            return ResponseEntity.ok().body(cgUserGameDataService.getTop10Highscore());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @GetMapping("/quests/active")
    public ResponseEntity<Object> getActiveQuests(Principal principal){
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(cgUserGameDataService.getActiveQuestInfo(principal.getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Quests nicht geladen", "Es sind keine Quests aktiv 🦥"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @GetMapping("/score")
    public ResponseEntity<Object> getScore(Principal principal){
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(cgUserGameDataService.getScore(principal.getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Score nicht gefunden", "Nichts gefunden für: " + principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @GetMapping("/achievements")
    public ResponseEntity<Object> getAchievementsForUserByUserId(Principal principal){
        try {
            //principal.getName() contains userId
            return ResponseEntity.ok().body(cgUserGameDataService.getAchievementsForUserByUserId(principal.getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Keine Achievements gefunden", "Nichts gefunden für: " + principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }

    }

}
