package com.github.jekattack.cornergame.game.quests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
        return questService.getActiveQuests(principal.getName());
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ArrayList<StartedQuest> startQuest(@RequestBody String questId, Principal principal){
        //principal.getName() contains userId
        return questService.startQuest(principal.getName(), questId);
    }
}
