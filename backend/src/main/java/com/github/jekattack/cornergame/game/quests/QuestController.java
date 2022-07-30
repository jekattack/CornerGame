package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Quest> getAllQuests(){
        return questService.getAllQuests();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Quest addQuest(@RequestBody Quest newQuest){
        return questService.addQuest(newQuest);
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public String startQuest(@RequestBody String questId, Principal principal){
        try{
            //principal.getName() contains userId
            return questService.startQuest(principal.getName(), questId);
        } catch (NoSuchElementException e){
            throw new NoSuchElementException("User or Quest not found");
        }
    }
}
