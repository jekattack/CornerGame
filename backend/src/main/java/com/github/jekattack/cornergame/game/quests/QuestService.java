package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.visits.Visit;
import com.github.jekattack.cornergame.game.visits.VisitObserver;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestService implements VisitObserver {

    private final QuestRepository questRepository;
    private final VisitRepository visitRepository;
    private final CGUserGameDataService cgUserGameDataService;
    private final List<QuestObserver> questObservers;

    @Override
    public void onVisitCreated(Visit visit, CGUserGameData userGameData) {

        if(visit.getQuestId()!=null){
            Optional<QuestItem> questItem = userGameData.getQuestItems().stream()
                    .filter(qi -> qi.getQuestId().equals(visit.getQuestId()) && qi.getQuestStatus().equals(QuestStatus.STARTED))
                    .findFirst();
            if(questItem.isPresent()){
                List<Visit> questVisits = visitRepository.findAllByQuestIdAndTimestampIsAfter(
                        visit.getQuestId(),questItem.get().getTimestamp());
                if(checkIfQuestComplete(questVisits, visit.getQuestId())){
                    Quest quest = questRepository.findById(visit.getQuestId()).orElseThrow();
                    questObservers.forEach(observer -> observer.onQuestCompleted(visit.getUserId(), quest));
                }
            }
        }
    }

    public Quest addQuest(Quest newQuest) {
        return questRepository.save(newQuest);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public String startQuest(String userId, String questId) {
        Quest quest = questRepository.findById(questId).orElseThrow();
        questObservers.forEach(observer -> observer.onQuestStarted(userId, quest));
        return "Quest " + quest.getName() + " gestartet!";
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
        for(ActiveQuestDTO questDTO : activeQuestsDTO){
            if (questRepository.existsByIdAndKioskGooglePlacesIdsContaining(
                    questDTO.getQuest().getId(),
                    googlePlacesId)){
                return Optional.of(questDTO.getQuest());
            }
        }
        return Optional.of(new Quest());
    }

    public boolean checkIfQuestComplete(List<Visit> visits, String questId){
        List<String> distinctVisits = visits.stream().map(Visit::getGooglePlacesId).distinct().toList();
        if(questRepository.findById(questId).isPresent()){
            return distinctVisits.size() == questRepository.findById(questId).get().getKioskGooglePlacesIds().length;
        } else {
            return false;
        }
    }


}
