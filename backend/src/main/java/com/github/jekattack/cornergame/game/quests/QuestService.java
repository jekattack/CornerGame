package com.github.jekattack.cornergame.game.quests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestService {

    private final QuestRepository questRepository;

    public ActiveQuestsDTO getActiveQuests(String username) {
        //Datenbankanfrage für alle Visits eines Users mit questId

        //Response abchecken, ob die den Quests zugehörigen Visits nicht länger her sind, als die maximale Duration des Quests

        return null;
    }
}
