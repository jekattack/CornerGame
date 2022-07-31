package com.github.jekattack.cornergame.game.achievements;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRepository;
import com.github.jekattack.cornergame.game.gamedata.achievements.Achievement;
import com.github.jekattack.cornergame.game.gamedata.achievements.AchievementRepository;
import com.github.jekattack.cornergame.game.gamedata.achievements.AchievementRequirements;
import com.github.jekattack.cornergame.game.gamedata.achievements.AchievementService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.visits.Visit;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import com.github.jekattack.cornergame.userdata.CGUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.*;

class AchievementServiceTest {

    AchievementRequirements achievementRequirements1 = new AchievementRequirements(0,0,0,1);
    Achievement achievement1 = Achievement.builder()
            .id("achievementId1")
            .name("achievement1")
            .description("This is achievement1.")
            .requirements(achievementRequirements1)
            .build();

    AchievementRequirements achievementRequirements2 = new AchievementRequirements(0,0,1,0);
    Achievement achievement2 = Achievement.builder()
            .id("achievementId2")
            .name("achievement2")
            .description("This is achievement2.")
            .requirements(achievementRequirements2)
            .build();

    AchievementRequirements achievementRequirements3 = new AchievementRequirements(0,1,0,0);
    Achievement achievement3 = Achievement.builder()
            .id("achievementId3")
            .name("achievement3")
            .description("This is achievement3.")
            .requirements(achievementRequirements3)
            .build();

    AchievementRequirements achievementRequirements4 = new AchievementRequirements(1,0,0,0);
    Achievement achievement4 = Achievement.builder()
            .id("achievementId4")
            .name("achievement4")
            .description("This is achievement4.")
            .requirements(achievementRequirements4)
            .build();

    QuestItem questItem0 = QuestItem.builder()
            .id("testQuestItemId0")
            .questId("testQuestId0")
            .questStatus(QuestStatus.DONE)
            .timestamp(Date.from(Instant.now()))
            .build();
    CGUserGameData gameData1 = CGUserGameData.builder()
            .id("gameDataId1")
            .userId("testUserId1")
            .score(100)
            .questItems(new ArrayList<>(List.of(questItem0)))
            .achievementIds(List.of())
            .build();

    Quest quest1 = Quest.builder()
            .id("questId1")
            .name("testQuest1")
            .description("This is TestQuest1!")
            .durationInMinutes(300)
            .kioskGooglePlacesIds(new String[0])
            .scoreMultiplier(2)
            .build();

    CGUser testUser1 = CGUser.builder()
            .id("testUserId1")
            .username("testUser1")
            .role("user")
            .build();

    Visit visit1 = Visit.builder()
            .id("testVisitId1")
            .questId("testQuestId0")
            .userId("testUserId1")
            .timestamp(Date.from(Instant.now()))
            .googlePlacesId("placesId1")
            .build();

    @Test
    void shouldReturnListWithAllAchievements() {
        //Given
        Achievement testAchievement1 = achievement1;
        Achievement testAchievement2 = achievement2;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findAll()).thenReturn(List.of(testAchievement1,testAchievement2));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        List<Achievement> expected = List.of(achievement1,achievement2);

        //When
        List<Achievement> actual = achievementService.getAllAchievements();

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldCreateAchievement() {
        //Given
        Achievement testAchievement1 = achievement1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.save(testAchievement1)).thenReturn(testAchievement1);
        CGUserGameDataRepository userGameDataRepository = Mockito.mock(CGUserGameDataRepository.class);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        //When
        Achievement actual = achievementService.createAchievement(testAchievement1);

        //Then
        Assertions.assertThat(actual).isEqualTo(testAchievement1);
    }

    @Test
    void shouldReturnAchievementWithCertainId() {
        //Given
        String testAchievement1Id = achievement1.getId();
        Achievement testAchievement1 = achievement1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findById(testAchievement1Id)).thenReturn(Optional.of(testAchievement1));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        Achievement expected = achievement1;

        //When
        Achievement actual = achievementService.getAchievementById(testAchievement1Id);

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowErrorBecauseNoAchievementWasFound() {
        //Given
        String testAchievement1Id = achievement1.getId();
        Achievement testAchievement1 = achievement1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findById(testAchievement1Id)).thenReturn(Optional.of(testAchievement1));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        Achievement expected = achievement1;

        //Then
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> achievementService.getAchievementById("wrongId"));
    }

    @Test
    void shouldDeleteAchievement() {
        //Given
        Achievement testAchievement1 = achievement1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.existsById(achievement1.getId())).thenReturn(true);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        //When
        achievementService.deleteAchievement(testAchievement1.getId());

        //Then
        Mockito.verify(achievementRepository).deleteById(testAchievement1.getId());
    }

    @Test
    void shouldNotDeleteAchievement() {
        //Given
        String wrongId = "notAchievement1";

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.existsById(wrongId)).thenReturn(false);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        //When
        achievementService.deleteAchievement(wrongId);

        //Then
        Mockito.verify(achievementRepository, Mockito.never()).deleteById(wrongId);
    }

    @Test
    void shouldTriggerOnAchievementReceivedWithOneQuest() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Achievement testAchievement = achievement2;
        Quest testQuest1 = quest1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findAllByRequirements(new AchievementRequirements(0,0,1,0))).thenReturn(List.of(testAchievement));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        CGUserGameData expectedTestGameData1 = CGUserGameData.builder()
                .id("gameDataId1")
                .userId("testUserId1")
                .score(100)
                .questItems(new ArrayList<>(List.of(questItem0)))
                .achievementIds(List.of(testAchievement.getId()))
                .build();

        //When
        achievementService.onQuestCompletedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository).save(expectedTestGameData1);
    }

    @Test
    void shouldNotTriggerOnAchievementReceived() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Quest testQuest1 = quest1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        //When
        achievementService.onQuestCompletedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository, Mockito.never()).save(testGameData1);
    }

    @Test
    void shouldTriggerOnAchievementReceivedWithOneQuestStarted() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Achievement testAchievement = achievement3;
        Quest testQuest1 = quest1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findAllByRequirements(new AchievementRequirements(0,1,0,0))).thenReturn(List.of(testAchievement));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        CGUserGameData expectedTestGameData1 = CGUserGameData.builder()
                .id("gameDataId1")
                .userId("testUserId1")
                .score(100)
                .questItems(new ArrayList<>(List.of(questItem0)))
                .achievementIds(List.of(testAchievement.getId()))
                .build();

        //When
        achievementService.onQuestStartedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository).save(expectedTestGameData1);
    }

    @Test
    void shouldNotTriggerOnAchievementReceivedWithOneQuestStarted() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Quest testQuest1 = quest1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        //When
        achievementService.onQuestStartedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository, Mockito.never()).save(testGameData1);
    }

    @Test
    void shouldTriggerOnVisitCreatedWithTwoAchievements() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Achievement testAchievement1 = achievement1;
        Visit testVisit1 = visit1;
        Achievement testAchievement4 = achievement4;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findAllByRequirements(new AchievementRequirements(1,0,0,0))).thenReturn(List.of(testAchievement4));
        Mockito.when(achievementRepository.findAllByRequirements(new AchievementRequirements(0,0,0,1))).thenReturn(List.of(testAchievement1));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        Mockito.when(visitRepository.findAllByUserId(testUserId1)).thenReturn(new ArrayList<>(List.of(testVisit1)));
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        CGUserGameData expectedTestGameData1 = CGUserGameData.builder()
                .id("gameDataId1")
                .userId("testUserId1")
                .score(100)
                .questItems(new ArrayList<>(List.of(questItem0)))
                .achievementIds(List.of(testAchievement4.getId(), testAchievement1.getId()))
                .build();

        //When
        achievementService.onVisitCreatedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository).save(expectedTestGameData1);
    }

    @Test
    void shouldTriggerOnVisitCreatedWithOneAchievement() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Achievement testAchievement1 = achievement1;
        Visit testVisit1 = visit1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        Mockito.when(achievementRepository.findAllByRequirements(new AchievementRequirements(0,0,0,1))).thenReturn(List.of(testAchievement1));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        Mockito.when(visitRepository.findAllByUserId(testUserId1)).thenReturn(new ArrayList<>(List.of(testVisit1)));
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        CGUserGameData expectedTestGameData1 = CGUserGameData.builder()
                .id("gameDataId1")
                .userId("testUserId1")
                .score(100)
                .questItems(new ArrayList<>(List.of(questItem0)))
                .achievementIds(List.of(testAchievement1.getId()))
                .build();

        //When
        achievementService.onVisitCreatedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository).save(expectedTestGameData1);
    }

    @Test
    void shouldNotTriggerOnVisitCreated() {
        //Given
        String testUserId1 = testUser1.getId();
        CGUserGameData testGameData1 = gameData1;
        Achievement testAchievement1 = achievement1;
        Visit testVisit1 = visit1;

        AchievementRepository achievementRepository = Mockito.mock(AchievementRepository.class);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        Mockito.when(visitRepository.findAllByUserId(testUserId1)).thenReturn(new ArrayList<>(List.of(testVisit1)));
        CGUserGameDataRepository gameDataRepository = Mockito.mock(CGUserGameDataRepository.class);

        AchievementService achievementService = new AchievementService(achievementRepository,visitRepository,gameDataRepository);

        //When
        achievementService.onVisitCreatedInGameData(testGameData1);

        //Then
        Mockito.verify(gameDataRepository, Mockito.never()).save(testGameData1);
    }

}