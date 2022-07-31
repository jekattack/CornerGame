package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.achievements.Achievement;

import com.github.jekattack.cornergame.game.achievements.AchievementRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserGameDataService implements VisitObserver, QuestObserver {

    private final CGUserGameDataRepository cgUserGameDataRepository;
    private final CGUserRepository cgUserRepository;
    private final QuestRepository questRepository;
    private final AchievementRepository achievementRepository;
    private final List<CGUserGameDataObserver> observers;

    public void createGameData(String userId) {
        cgUserGameDataRepository.save(new CGUserGameData(userId));
    }

    public void save(CGUserGameData userGameData) {
        cgUserGameDataRepository.save(userGameData);
    }

    public CGUserGameDataDTO getScore(String userId) {
        CGUser user = cgUserRepository.findById(userId).orElseThrow();
        CGUserGameData gameData = cgUserGameDataRepository.findByUserId(user.getId()).orElseThrow();
        return new CGUserGameDataDTO(user.getUsername(), gameData.getScore());
    }

    public Optional<CGUserGameData> getByUserId(String userId) {
        return cgUserGameDataRepository.findByUserId(userId);
    }

    public List<Achievement> getAchievementsForUserByUserId(String userId) {
        CGUserGameData gameData = cgUserGameDataRepository.findByUserId(userId).orElseThrow();
        List<String> achievementIds = gameData.getAchievementIds();
        List<Achievement> achievementResponse = new ArrayList<>();
        for (String achievementId : achievementIds) {
            Optional<Achievement> achievement = achievementRepository.findById(achievementId);
            if (achievement.isPresent()) {
                achievementResponse.add(achievement.get());
            }
        }
        return achievementResponse;
    }

    public ArrayList<CGUserGameDataDTO> getTop10Highscore() {
        ArrayList<CGUserGameData> top10GameData = cgUserGameDataRepository.findTop10ByOrderByScoreDesc();
        ArrayList<CGUserGameDataDTO> top10GameDataExport = new ArrayList<>();
        for (CGUserGameData gameData : top10GameData) {
            CGUserGameDataDTO gameDataDTO = new CGUserGameDataDTO(cgUserRepository
                    .findById(gameData.getUserId()).orElseThrow().getUsername(),
                    gameData.getScore());
            top10GameDataExport.add(gameDataDTO);
        }
        return top10GameDataExport;
    }

    public Optional<Quest> getActiveQuestForKiosk(String userId, String googlePlacesId) {
        List<Quest> quests = getActiveQuests(userId);
        Optional<Quest> expectedQuest = questRepository.findByKioskGooglePlacesIdsContaining(googlePlacesId);
        if (!quests.isEmpty() && expectedQuest.isPresent()) {
            for (Quest quest : quests) {
                if (quest.equals(expectedQuest.get())) {
                    return Optional.of(quest);
                }
            }
        }
        return Optional.empty();
    }

    public List<ActiveQuestDTO> getActiveQuestInfo(String userId) {
        List<Quest> activeQuests = getActiveQuests(userId);
        List<QuestItem> activeQuestItems = getActiveQuestItems(userId);
        List<ActiveQuestDTO> activeQuestDTOs = new ArrayList<>();
        if (!activeQuestItems.isEmpty()) {
            for (Quest activeQuest : activeQuests) {
                QuestItem questItemResult = activeQuestItems.stream()
                        .filter(questItem -> questItem.getQuestId().equals(activeQuest.getId()))
                        .toList().get(0);
                activeQuestDTOs.add(new ActiveQuestDTO(activeQuest, checkMinutesLeft(questItemResult)));
            }
        }
        return activeQuestDTOs;
    }

    public List<Quest> getActiveQuests(String userId) {
        List<QuestItem> questItems = getActiveQuestItems(userId);
        List<Quest> quests = new ArrayList<>();
        for (QuestItem questItem : questItems) {
            Optional<Quest> questOptional = questRepository.findById(questItem.getQuestId());
            questOptional.ifPresent(quests::add);
        }
        return quests;
    }

    public List<QuestItem> getActiveQuestItems(String userId) {
        CGUserGameData gameData = refreshQuestItemsStatus(userId);
        List<QuestItem> activeQuestItems = new ArrayList<>();
        if (!(gameData.getQuestItems().isEmpty())) {
            activeQuestItems = gameData.getQuestItems().stream().filter(questItem -> questItem.getQuestStatus().equals(QuestStatus.STARTED)).toList();
        }
        return activeQuestItems;
    }

    public CGUserGameData scoreForNewVisit(String userId) {
        CGUserGameData userGameData = cgUserGameDataRepository.findByUserId(userId).orElseThrow();
        userGameData.setScore(userGameData.getScore() + 100);
        log.info(userId + ": 100 Points added for new Visit");
        return cgUserGameDataRepository.save(userGameData);
    }

    public CGUserGameData scoreForQuestAndMarkAsDone(String userId, Quest quest) {
        CGUserGameData userGameData = cgUserGameDataRepository.findByUserId(userId).orElseThrow();
        Optional<QuestItem> questItem = userGameData.getQuestItems().stream()
                .filter(qi -> qi.getQuestId().equals(quest.getId()))
                .findFirst();
        questItem.ifPresent(item -> item.setQuestStatus(QuestStatus.DONE));

        int numberOfVisitsForQuest = quest.getKioskGooglePlacesIds().length;
        int pointsToAdd = numberOfVisitsForQuest * 100 * quest.getScoreMultiplier() - numberOfVisitsForQuest * 100;
        userGameData.setScore(userGameData.getScore() + pointsToAdd);

        log.info(userId + ": " + pointsToAdd + " Points added for new Visit");
        return cgUserGameDataRepository.save(userGameData);
    }

    public CGUserGameData refreshQuestItemsStatus(String userId) {
        CGUserGameData userGameData = cgUserGameDataRepository.findByUserId(userId).orElseThrow();
        if (userGameData.getQuestItems() != null || !userGameData.getQuestItems().isEmpty()) {
            List<QuestItem> activeQuests = userGameData.getQuestItems().stream().filter(quest -> quest.getQuestStatus().equals(QuestStatus.STARTED)).toList();
            for (QuestItem quest : activeQuests) {
                int minutesLeft = checkMinutesLeft(quest);
                if (quest.getQuestStatus() != QuestStatus.DONE && minutesLeft <= 0) {
                    quest.setQuestStatus(QuestStatus.EXPIRED);
                }
            }
        }
        return cgUserGameDataRepository.save(userGameData);
    }

    public int checkMinutesLeft(QuestItem questItem) {
        Quest quest = questRepository.findById(questItem.getQuestId()).orElseThrow();
        Instant timeLeft = questItem.getTimestamp().toInstant()
                .plus(quest.getDurationInMinutes(), ChronoUnit.MINUTES)
                .minus(Instant.now().toEpochMilli() / 1000 / 60, ChronoUnit.MINUTES);

        return Math.toIntExact(timeLeft.toEpochMilli() / 1000 / 60);
    }

    @Override
    public void onVisitCreated(Visit visit, CGUserGameData gameData) {
        CGUserGameData newGameData = scoreForNewVisit(visit.getUserId());
        observers.forEach(observer -> observer.onVisitCreatedInGameData(newGameData));
    }

    @Override
    public void onQuestCompleted(String userId, Quest quest) {
        CGUserGameData newGameData = scoreForQuestAndMarkAsDone(userId, quest);
        observers.forEach(observer -> observer.onQuestCompletedInGameData(newGameData));
    }

    @Override
    public void onQuestStarted(String userId, Quest quest) {
        CGUserGameData gameData = cgUserGameDataRepository.findByUserId(userId).orElseThrow();
        List<QuestItem> questItems = gameData.getQuestItems();
        questItems.add(new QuestItem(quest.getId(), Date.from(Instant.now())));
        gameData.setQuestItems(questItems);
        CGUserGameData newGameData = cgUserGameDataRepository.save(gameData);
        observers.forEach(observer -> observer.onQuestStartedInGameData(newGameData));
    }

    @Override
    public void onQuestCanceled(String userId, Quest quest) {
        CGUserGameData gameData = cgUserGameDataRepository.findByUserId(userId).orElseThrow();
        gameData.getQuestItems().stream()
                .filter(qi -> qi.getQuestId().equals(quest.getId()) && qi.getQuestStatus().equals(QuestStatus.STARTED))
                .findFirst().orElseThrow()
                .setQuestStatus(QuestStatus.EXPIRED);
        cgUserGameDataRepository.save(gameData);
    }

}
