package com.github.jekattack.cornergame.game.achievements;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService implements VisitObserver, QuestObserver {

    private final AchievementRepository achievementRepository;
    private final CGUserGameDataRepository gameDataRepository;
    private final VisitRepository visitRepository;
    private final List<AchievementObserver> achievementObservers;

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
    public void onQuestCompleted(String userId, Quest quest) {
        Optional<CGUserGameData> gameData = gameDataRepository.findByUserId(userId);
        if(gameData.isPresent()){
            List<QuestItem> doneQuests = gameData.get().getQuestItems().stream().filter(questItem -> questItem.getQuestStatus().equals(QuestStatus.DONE)).toList();
            List<Achievement> achievements = achievementRepository.findAllByQuestsFinished(doneQuests.size());
            if(!achievements.isEmpty()){
                List<String> achievementIds = achievements.stream().map(Achievement::getId).toList();
                achievementObservers.forEach(observer -> observer.onAchievementReceived(achievementIds, userId));
            }
        }
    }

    @Override
    public void onQuestStarted(String userId, Quest quest) {
        Optional<CGUserGameData> gameData = gameDataRepository.findByUserId(userId);
        if(gameData.isPresent()){
            List<QuestItem> startedQuests = gameData.get().getQuestItems();
            List<Achievement> achievements = achievementRepository.findAllByQuestsStarted(startedQuests.size());
            if(!achievements.isEmpty()){
                List<String> achievementIds = achievements.stream().map(Achievement::getId).toList();
                achievementObservers.forEach(observer -> observer.onAchievementReceived(achievementIds, userId));
            }
        }
    }

    @Override
    public void onVisitCreated(Visit visit, CGUserGameData cgUserGameData) {
        List<Visit> allVisits = visitRepository.findAllByUserId(visit.getUserId());
        if(!allVisits.isEmpty()){
            List<Achievement> achievements;
            List<String> kiosksVisited = allVisits.stream().map(Visit::getGooglePlacesId).distinct().toList();
            achievements = Stream.concat(
                    achievementRepository.findAllByVisitsCreated(allVisits.size()).stream(),
                    achievementRepository.findAllByKiosksVisited(kiosksVisited.size()).stream()
            ).toList();
            if(!achievements.isEmpty()){
                List<String> achievementIds = achievements.stream().map(Achievement::getId).toList();
                achievementObservers.forEach(observer -> observer.onAchievementReceived(achievementIds, visit.getUserId()));
            }
        }
    }
}
