package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.quests.QuestObserver;
import com.github.jekattack.cornergame.game.quests.QuestRepository;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.visits.Visit;
import com.github.jekattack.cornergame.game.visits.VisitObserver;
import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserGameDataService implements VisitObserver, QuestObserver {

    private final CGUserGameDataRespository cgUserGameDataRespository;
    private final CGUserRepository cgUserRepository;
    private final QuestRepository questRepository;

    public void createGameData(String userId) {
        cgUserGameDataRespository.save(new CGUserGameData(userId));
    }
    public void save(CGUserGameData userGameData) {
        cgUserGameDataRespository.save(userGameData);
    }
    public CGUserGameDataDTO getScore(String userId) {
        CGUser user = cgUserRepository.findById(userId).orElseThrow();
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(user.getId()).orElseThrow();
        return new CGUserGameDataDTO(user.getUsername(), gameData.getScore());
    }
    public Optional<CGUserGameData> getByUserId(String userId) {
        return cgUserGameDataRespository.findByUserId(userId);
    }
    public ArrayList<CGUserGameDataDTO> getTop10Highscore() {
        ArrayList<CGUserGameData> top10GameData = cgUserGameDataRespository.findTop10ByOrderByScoreDesc();
        ArrayList<CGUserGameDataDTO> top10GameDataExport = new ArrayList<>();
        for(CGUserGameData gameData : top10GameData){
            CGUserGameDataDTO gameDataDTO = new CGUserGameDataDTO(cgUserRepository
                    .findById(gameData.getUserId()).orElseThrow().getUsername(),
                    gameData.getScore());
            top10GameDataExport.add(gameDataDTO);
        }
        return top10GameDataExport;
    }

    public Quest getActiveQuestForKiosk(String userId, String googlePlacesId) {
        List<Quest> quests = getActiveQuests(userId);
        Optional<Quest> expectedQuest = questRepository.findByKioskGooglePlacesIdsContaining(googlePlacesId);
        if(!quests.isEmpty() && expectedQuest.isPresent()){
            for(Quest quest : quests){
                if(quest.equals(expectedQuest.get())){
                    return quest;
                }
            }
        }
        return null;
    }

    public List<ActiveQuestDTO> getActiveQuestInfo(String userId){
        List<Quest> activeQuests = getActiveQuests(userId);
        List<QuestItem> activeQuestItems = getActiveQuestItems(userId);
        List<ActiveQuestDTO> activeQuestDTOs = new ArrayList<>();
        if(!activeQuestItems.isEmpty()){
            for(Quest activeQuest : activeQuests){
                QuestItem questItemResult = activeQuestItems.stream()
                        .filter(questItem -> questItem.getQuestId().equals(activeQuest.getId()))
                        .toList().get(0);
                activeQuestDTOs.add(new ActiveQuestDTO(activeQuest, checkMinutesLeft(questItemResult)));
            }
        }
        return activeQuestDTOs;
    }

    public List<Quest> getActiveQuests(String userId){
        List<QuestItem> questItems = getActiveQuestItems(userId);
        List<Quest> quests = new ArrayList<>();
        for(QuestItem questItem : questItems){
            Optional<Quest> questOptional = questRepository.findById(questItem.getQuestId());
            questOptional.ifPresent(quests::add);
        }
        return quests;
    }

    public List<QuestItem> getActiveQuestItems(String userId){
        CGUserGameData gameData = refreshQuestItemsStatus(userId);
        return gameData.getQuestItems().stream().filter(questItem -> questItem.getQuestStatus().equals(QuestStatus.STARTED)).toList();
    }

    public void scoreForNewVisit(String userId) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        userGameData.setScore(userGameData.getScore() + 100);
        cgUserGameDataRespository.save(userGameData);
        log.info(userId + ": 100 Points added for new Visit");
    }

    public void scoreForQuestAndMarkAsDone(String userId, Quest quest) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        Optional<QuestItem> questItem = userGameData.getQuestItems().stream()
                .filter(qi -> qi.getQuestId().equals(quest.getId()))
                .findFirst();
        questItem.ifPresent(item -> item.setQuestStatus(QuestStatus.DONE));

        int numberOfVisitsForQuest = quest.getKioskGooglePlacesIds().length;
        int pointsToAdd = numberOfVisitsForQuest * 100 * quest.getScoreMultiplier() - numberOfVisitsForQuest * 100;
        userGameData.setScore(userGameData.getScore() + pointsToAdd);

        cgUserGameDataRespository.save(userGameData);
        log.info(userId + ": " + pointsToAdd + " Points added for new Visit");
    }
    public CGUserGameData refreshQuestItemsStatus(String userId){
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        if(userGameData.getQuestItems()!=null || !userGameData.getQuestItems().isEmpty()){
            List<QuestItem> activeQuests = userGameData.getQuestItems().stream().filter(quest -> quest.getQuestStatus().equals(QuestStatus.STARTED)).toList();
            for(QuestItem quest : activeQuests){
                int minutesLeft = checkMinutesLeft(quest);
                if(quest.getQuestStatus()!=QuestStatus.DONE && minutesLeft <= 0){
                    quest.setQuestStatus(QuestStatus.EXPIRED);
                }
            }
        }
        return cgUserGameDataRespository.save(userGameData);
    }
    public int checkMinutesLeft(QuestItem questItem){
        Quest quest = questRepository.findById(questItem.getQuestId()).orElseThrow();
        Instant timeLeft = questItem.getTimestamp().toInstant()
                .plus(quest.getDurationInMinutes(), ChronoUnit.MINUTES)
                .minus(Instant.now().toEpochMilli()/1000/60, ChronoUnit.MINUTES);

        return Math.toIntExact(timeLeft.toEpochMilli()/1000/60);
    }

    @Override
    public void onVisitCreated(Visit visit) {
        scoreForNewVisit(visit.getUserId());
    }

    @Override
    public void onQuestCompleted(String userId, Quest quest) {
        scoreForQuestAndMarkAsDone(userId, quest);
    }

    @Override
    public void onQuestStarted(String userId, Quest quest) {
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        ArrayList<QuestItem> questItems = gameData.getQuestItems();
        questItems.add(new QuestItem(quest.getId(), Date.from(Instant.now())));
        gameData.setQuestItems(questItems);
        cgUserGameDataRespository.save(gameData);
    }
}
