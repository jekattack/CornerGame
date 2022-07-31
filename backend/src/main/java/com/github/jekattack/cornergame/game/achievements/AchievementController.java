package com.github.jekattack.cornergame.game.achievements;

import org.springframework.dao.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Achievement> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Achievement createAchievement(@RequestBody Achievement achievement){
        try{
            return achievementService.createAchievement(achievement);
        } catch (DuplicateKeyException e){
            throw e;
        }
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Achievement getAchievementById(@PathVariable String id){
        try{
            return achievementService.getAchievementById(id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Kein Achievement für " + id + "gefunden.");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.GONE)
    public void deleteAchievement(@PathVariable String id){
        achievementService.deleteAchievement(id);
    }

}
