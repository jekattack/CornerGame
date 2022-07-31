package com.github.jekattack.cornergame.game.gamedata.achievements;

import com.github.jekattack.cornergame.model.CGErrorDTO;
import org.springframework.dao.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping()
    public ResponseEntity<Object> getAllAchievements() {
        try {
            return ResponseEntity.ok().body(achievementService.getAllAchievements());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> createAchievement(@RequestBody Achievement achievement){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(achievementService.createAchievement(achievement));
        } catch (DuplicateKeyException e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO("Achievement not created", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getAchievementById(@PathVariable String id){
        try {
            return ResponseEntity.ok().body(achievementService.getAchievementById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO("Achievement not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.GONE)
    public void deleteAchievement(@PathVariable String id){
        achievementService.deleteAchievement(id);
    }

}
