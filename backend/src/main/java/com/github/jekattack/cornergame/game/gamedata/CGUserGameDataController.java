package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/gamedata")
@RequiredArgsConstructor
public class CGUserGameDataController {

    private final CGUserGameDataService cgUserGameDataService;

    @GetMapping("/highscore")
    @ResponseStatus(HttpStatus.OK)
    public List<CGUserGameDataDTO> getTop10Highscore(){
        return cgUserGameDataService.getTop10Highscore();
    }

    @GetMapping("/quests/active")
    @ResponseStatus(HttpStatus.OK)
    public List<ActiveQuestDTO> getActiveQuests(Principal principal){
        try{
            //principal.getName() contains userId
            return cgUserGameDataService.getActiveQuestInfo(principal.getName());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Active quests could not be loaded.");
        }
    }

    @GetMapping("/score")
    @ResponseStatus(HttpStatus.OK)
    public CGUserGameDataDTO getScore(Principal principal){
        try{
            //principal.getName() contains userId
            return cgUserGameDataService.getScore(principal.getName());
        } catch(NoSuchElementException e){
            throw new NoSuchElementException("No score found for User-Id:" + principal.getName());
        }
    }

}
