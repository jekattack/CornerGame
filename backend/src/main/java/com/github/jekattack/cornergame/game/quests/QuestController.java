package com.github.jekattack.cornergame.game.quests;

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

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ActiveQuestDTO> getActiveQuests(Principal principal){
        try{
            //principal.getName() contains userId
            return questService.getActiveQuests(principal.getName());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Active quests could not be found. User or at least one started Quest invalid.");
        }
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ArrayList<StartedQuest> startQuest(@RequestBody String questId, Principal principal){
        try{
            //principal.getName() contains userId
            return questService.startQuest(principal.getName(), questId);
        } catch (NoSuchElementException e){
            throw new NoSuchElementException("User or Quest not found");
        }
    }
}
