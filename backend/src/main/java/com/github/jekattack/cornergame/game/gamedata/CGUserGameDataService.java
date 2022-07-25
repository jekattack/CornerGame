package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.quests.QuestRepository;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserGameDataService {

    private final CGUserGameDataRespository cgUserGameDataRespository;
    private final CGUserRepository cgUserRepository;
    private final QuestRepository questRepository;

    public void createGameData(String userId) {
        cgUserGameDataRespository.save(new CGUserGameData(userId));
    }

    public CGUserGameDataDTO getScore(String userId) {
        CGUser user = cgUserRepository.findById(userId).orElseThrow();
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(user.getId()).orElseThrow();
        return new CGUserGameDataDTO(user.getUsername(), gameData.getScore());
    }

    public void scoreForNewVisit(String userId) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        userGameData.setScore(userGameData.getScore() + 100);
        cgUserGameDataRespository.save(userGameData);
        log.info(userId + ": 100 Points added for new Visit");
    }
    public int scoreForQuest(String userId, int scoreMultiplier, int numberOfVisitsForQuest) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        int pointsToAdd = numberOfVisitsForQuest * 100 * scoreMultiplier - numberOfVisitsForQuest * 100;
        userGameData.setScore(userGameData.getScore() + pointsToAdd);
        cgUserGameDataRespository.save(userGameData);
        log.info(userId + ": " + pointsToAdd + " Points added for new Visit");
        return pointsToAdd;
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

    public Optional<CGUserGameData> getByUserId(String userId) {
        return cgUserGameDataRespository.findByUserId(userId);
    }

    public void save(CGUserGameData userGameData) {
        cgUserGameDataRespository.save(userGameData);
    }

    public void scoreForQuestAndMarkAsDone(String userId, Quest quest) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        userGameData.getQuestItems().stream()
                .filter(qi -> qi.getQuestId().equals(quest.getId()))
                .findFirst().get().setQuestStatus(QuestStatus.DONE);
        save(userGameData);
        scoreForQuest(userId, quest.getScoreMultiplier(), quest.getKioskGooglePlacesIds().length);
    }
}
