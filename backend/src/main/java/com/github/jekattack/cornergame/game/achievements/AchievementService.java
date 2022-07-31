package com.github.jekattack.cornergame.game.achievements;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataObserver;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRepository;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.quests.QuestObserver;
import com.github.jekattack.cornergame.game.visits.Visit;
import com.github.jekattack.cornergame.game.visits.VisitObserver;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService implements CGUserGameDataObserver {

    private final AchievementRepository achievementRepository;
    private final VisitRepository visitRepository;
    private final CGUserGameDataRepository gameDataRepository;

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Achievement createAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public Achievement getAchievementById(String id) {
        return achievementRepository.findById(id).orElseThrow();
    }

    public void deleteAchievement(String id) {
        if(achievementRepository.existsById(id)){
            achievementRepository.deleteById(id);
        }
    }

    @Override
    public void onQuestStartedInGameData(CGUserGameData gameData) {
        List<QuestItem> startedQuests = gameData.getQuestItems();
        List<Achievement> achievements = achievementRepository.findAllByRequirements(
                new AchievementRequirements(0, startedQuests.size(),0,0));
        if(!achievements.isEmpty()){
            List<String> achievementIds = achievements.stream().map(Achievement::getId).toList();
            storeAchievement(achievementIds, gameData);
        }
    }

    @Override
    public void onQuestCompletedInGameData(CGUserGameData gameData) {
        List<QuestItem> doneQuests = gameData.getQuestItems().stream().filter(questItem -> questItem.getQuestStatus().equals(QuestStatus.DONE)).toList();
        List<Achievement> achievements = achievementRepository.findAllByRequirements(new AchievementRequirements(0,0,doneQuests.size(),0));
        if(!achievements.isEmpty()){
            List<String> achievementIds = achievements.stream().map(Achievement::getId).toList();
            storeAchievement(achievementIds, gameData);
        }
    }

    @Override
    public void onVisitCreatedInGameData(CGUserGameData gameData) {
        List<Visit> allVisits = visitRepository.findAllByUserId(gameData.getUserId());
        if(!allVisits.isEmpty()){
            List<String> kiosksVisited = allVisits.stream().map(Visit::getGooglePlacesId).distinct().toList();
            List<Achievement> achievements = Stream.concat(
                    achievementRepository.findAllByRequirements(
                                    new AchievementRequirements(allVisits.size(), 0,0,0)
                            )
                            .stream(),
                    achievementRepository.findAllByRequirements(
                                    new AchievementRequirements(0,0,0, kiosksVisited.size())
                            )
                            .stream()
            ).toList();
            if(!achievements.isEmpty()){
                List<String> achievementIds = achievements.stream().map(Achievement::getId).toList();
                storeAchievement(achievementIds, gameData);
            }
        }
    }

    public void storeAchievement(List<String> achievementIds, CGUserGameData gameData){
        for (String achievementId : achievementIds) {
            if (!gameData.getAchievementIds().contains(achievementId)) {
                List<String> oldAchievementIds = new ArrayList<>(gameData.getAchievementIds());
                oldAchievementIds.add(achievementId);
                gameData.setAchievementIds(oldAchievementIds);
                log.info("Neues Achievement hinzugefügt!");
            }
        }
        gameDataRepository.save(gameData);
    }
}
