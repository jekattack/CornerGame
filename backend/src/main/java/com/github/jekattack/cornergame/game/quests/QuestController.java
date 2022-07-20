package com.github.jekattack.cornergame.game.quests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    public ActiveQuestsDTO getActiveQuests(Principal principal){
        questService.getActiveQuests(principal.getName());
        return null;
    }
}
