package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRepository;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.visits.Visit;
import com.github.jekattack.cornergame.game.visits.VisitObserver;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestService implements VisitObserver {

    private final QuestRepository questRepository;
    private final VisitRepository visitRepository;
    private final CGUserGameDataRepository gameDataRepository;
    private final List<QuestObserver> questObservers;


    public Quest addQuest(Quest newQuest) {
        try{
            return questRepository.save(newQuest);
        } catch (DuplicateKeyException e){
            throw e;
        }
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public QuestEventDTO startQuest(String userId, String questId) {
        Quest quest = questRepository.findById(questId).orElseThrow();
        CGUserGameData gameData = gameDataRepository.findByUserId(userId).orElseThrow();
        Optional<QuestItem> questItem = gameData.getQuestItems().stream()
                .filter(qi-> qi.getQuestId().equals(questId) && qi.getQuestStatus().equals(QuestStatus.STARTED))
                .findFirst();
        if(questItem.isPresent()){
            throw new IllegalStateException("Quest wurde bereits gestartet.");
        }
        questObservers.forEach(observer -> observer.onQuestStarted(userId, quest));
        return new QuestEventDTO("Quest " + quest.getName() + " gestartet!", quest);
    }

    public QuestEventDTO cancelQuest(String userId, String questId) {
        Quest quest = questRepository.findById(questId).orElseThrow();
        CGUserGameData gameData = gameDataRepository.findByUserId(userId).orElseThrow();
        Optional<QuestItem> questItem = gameData.getQuestItems().stream()
                .filter(qi-> qi.getQuestId().equals(questId) && qi.getQuestStatus().equals(QuestStatus.STARTED))
                .findFirst();
        if(questItem.isPresent()){
            questObservers.forEach(questObserver -> questObserver.onQuestCanceled(userId, quest));
            return new QuestEventDTO("Quest " + quest.getName() + " abgebrochen.", quest);
        }
        throw new IllegalStateException("Quest nicht gestartet oder schon abgelaufen.");
    }


    private boolean checkIfQuestComplete(List<Visit> visits, String questId){
        List<String> distinctVisits = visits.stream().map(Visit::getGooglePlacesId).distinct().toList();
        if(questRepository.findById(questId).isPresent()){
            return distinctVisits.size() == questRepository.findById(questId).get().getKioskGooglePlacesIds().length;
        } else {
            return false;
        }
    }

    @Override
    public void onVisitCreated(Visit visit, CGUserGameData gameData) {
        if(visit.getQuestId()!=null){
            Optional<QuestItem> questItem = gameData.getQuestItems().stream()
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
}
