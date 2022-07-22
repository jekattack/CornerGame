package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestService {

    private final QuestRepository questRepository;
    private final CGUserGameDataRespository cgUserGameDataRespository;

    public Quest addQuest(Quest newQuest) {
        return questRepository.save(newQuest);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public ArrayList<StartedQuest> startQuest(String userId, String questId) {
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        Quest questToStart = questRepository.findById(questId).orElseThrow();
        ArrayList<StartedQuest> startedQuests = gameData.getStartedQuests();
        startedQuests.add(new StartedQuest(questToStart.getId(), Date.from(Instant.now())));
        gameData.setStartedQuests(startedQuests);
        return cgUserGameDataRespository.save(gameData).getStartedQuests();
    }


    public ArrayList<ActiveQuestDTO> getActiveQuests(String userId) {
        //Datenbankanfrage für alle Visits eines Users mit questId
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();

        ArrayList<StartedQuest> startedQuests = gameData.getStartedQuests();
        ArrayList<ActiveQuestDTO> startedQuestsResponse = new ArrayList<>();

        try {
            //Check, welche Quests noch aktiv sind
            for(StartedQuest startedQuest : startedQuests){
                Quest quest = questRepository.findById(startedQuest.getQuestId()).orElseThrow();
                Instant timeLeft = startedQuest.getTimestamp().toInstant()
                        .plus(quest.getDurationInMinutes(), ChronoUnit.MINUTES)
                        .minus(Instant.now().toEpochMilli()/1000/60, ChronoUnit.MINUTES);

                int minutesLeft = Math.toIntExact(timeLeft.toEpochMilli()/1000/60);

                if(minutesLeft <= 0){
                    startedQuests.remove(startedQuest);
                    continue;
                }
                startedQuestsResponse.add(new ActiveQuestDTO(quest, minutesLeft));
            }

            return startedQuestsResponse;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("At least one started quest could not be found");
        }
    }
}
