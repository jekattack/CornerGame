package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.visits.Visit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestService {

    private final QuestRepository questRepository;
    private final CGUserGameDataRespository cgUserGameDataRespository;
    private final CGUserGameDataService cgUserGameDataService;

    public Quest addQuest(Quest newQuest) {
        return questRepository.save(newQuest);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public ArrayList<QuestItem> startQuest(String userId, String questId) {
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        Quest questToStart = questRepository.findById(questId).orElseThrow();
        ArrayList<QuestItem> questItems = gameData.getQuestItems();
        questItems.add(new QuestItem(questToStart.getId(), Date.from(Instant.now())));
        gameData.setQuestItems(questItems);
        return cgUserGameDataRespository.save(gameData).getQuestItems();
    }


    public ArrayList<ActiveQuestDTO> getActiveQuests(String userId) {

        CGUserGameData gameData = cgUserGameDataService.refreshQuestItemsStatus(userId);

        List<QuestItem> questItems = gameData.getQuestItems().stream().filter(questItem -> questItem.getQuestStatus().equals(QuestStatus.STARTED)).toList();
        ArrayList<ActiveQuestDTO> startedQuestsResponse = new ArrayList<>();

        try {
            //Check, welche Quests noch aktiv sind
            for(QuestItem questItem : questItems){
                int minutesLeft = cgUserGameDataService.checkMinutesLeft(questItem);
                Quest quest = questRepository.findById(questItem.getQuestId()).orElseThrow();
                startedQuestsResponse.add(new ActiveQuestDTO(quest, minutesLeft));
            }

            return startedQuestsResponse;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("At least one started quest could not be found");
        }
    }

    public Optional<Quest> returnActiveQuestWithKiosk(String userId, String googlePlacesId) {
        ArrayList<ActiveQuestDTO> activeQuestsDTO = getActiveQuests(userId);
        Optional<Quest> activeQuestWithKiosk = Optional.of(new Quest());
        for(ActiveQuestDTO questDTO : activeQuestsDTO){
            if (questRepository.existsByIdAndKioskGooglePlacesIdsContaining(
                    questDTO.getQuest().getId(),
                    googlePlacesId)){
                return activeQuestWithKiosk = Optional.of(questDTO.getQuest());
            }
        }
        return activeQuestWithKiosk;
    }

    public boolean checkIfQuestComplete(List<Visit> visits, String questId){
        List<String> distinctVisits = visits.stream().map(Visit::getGooglePlacesId).distinct().toList();
        return distinctVisits.size() == questRepository.findById(questId).get().getKioskGooglePlacesIds().length;
    }


}
