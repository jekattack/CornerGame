package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.quests.QuestRepository;
import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class CGUserGameDataServiceTest {

    @Test
    void shouldCreateNewGameDataForUser(){
        //Given
        CGUser testUser = CGUser.builder().id("testId").username("testUsername").build();
        CGUserGameData testGameData = CGUserGameData.builder().id("gameDataId").userId("testId").score(0).build();
        CGUserGameDataRespository testGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(testGameDataRepository.findByUserId("testId")).thenReturn(Optional.of(testGameData));
        CGUserRepository testUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(testUserRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));
        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository, testQuestRepository);

        CGUserGameData expected = new CGUserGameData("testId");

        //When
        testGameDataService.createGameData(testUser.getId());

        //Then
        Mockito.verify(testGameDataRepository).save(expected);

    }

    @Test
    void shouldAdd100Points(){
        //Given
        CGUser testUser = CGUser.builder().id("testId").username("testUsername").build();
        CGUserGameData testGameData = CGUserGameData.builder().id("gameDataId").userId("testId").score(0).build();
        CGUserGameDataRespository testGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(testGameDataRepository.findByUserId("testId")).thenReturn(Optional.of(testGameData));
        CGUserRepository testUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(testUserRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));
        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository, testQuestRepository);

        CGUserGameData expected = testGameData;
        expected.setScore(expected.getScore()+100);
        //When
        testGameDataService.scoreForNewVisit(testUser.getId());

        //Then
        Assertions.assertThat(testGameData.getScore()).isEqualTo(expected.getScore());
        Mockito.verify(testGameDataRepository).save(testGameData);
    }

    @Test
    void shouldGetHighscoreForTop10Users(){
        //Given
        CGUser testUser1 = CGUser.builder().id("testId1").username("testUsername1").build();
        CGUser testUser2 = CGUser.builder().id("testId2").username("testUsername2").build();
        CGUser testUser3 = CGUser.builder().id("testId3").username("testUsername3").build();
        CGUser testUser4 = CGUser.builder().id("testId4").username("testUsername4").build();
        CGUserGameData testGameData1 = CGUserGameData.builder().id("gameDataId1").userId("testId1").score(400).build();
        CGUserGameData testGameData2 = CGUserGameData.builder().id("gameDataId2").userId("testId2").score(300).build();
        CGUserGameData testGameData3 = CGUserGameData.builder().id("gameDataId3").userId("testId3").score(100).build();
        CGUserGameData testGameData4 = CGUserGameData.builder().id("gameDataId4").userId("testId4").score(0).build();
        CGUserGameDataDTO testGameDataDTO1 = new CGUserGameDataDTO(testUser1.getUsername(),testGameData1.getScore());
        CGUserGameDataDTO testGameDataDTO2 = new CGUserGameDataDTO(testUser2.getUsername(),testGameData2.getScore());
        CGUserGameDataDTO testGameDataDTO3 = new CGUserGameDataDTO(testUser3.getUsername(),testGameData3.getScore());
        CGUserGameDataDTO testGameDataDTO4 = new CGUserGameDataDTO(testUser4.getUsername(),testGameData4.getScore());

        CGUserGameDataRespository testGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(testGameDataRepository.findTop10ByOrderByScoreDesc()).thenReturn(new ArrayList<>(List.of(testGameData1, testGameData2, testGameData3, testGameData4)));

        CGUserRepository testUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(testUserRepository.findById("testId1")).thenReturn(Optional.of(testUser1));
        Mockito.when(testUserRepository.findById("testId2")).thenReturn(Optional.of(testUser2));
        Mockito.when(testUserRepository.findById("testId3")).thenReturn(Optional.of(testUser3));
        Mockito.when(testUserRepository.findById("testId4")).thenReturn(Optional.of(testUser4));

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository, testQuestRepository);


        //When
        ArrayList<CGUserGameDataDTO> expected = new ArrayList<>(List.of(testGameDataDTO1,testGameDataDTO2,testGameDataDTO3,testGameDataDTO4));
        ArrayList<CGUserGameDataDTO> actual = testGameDataService.getTop10Highscore();

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldGetScoreForUserId(){
        //Given
        CGUser testUser = CGUser.builder()
                .id("testUserId")
                .username("testusername")
                .email("test@mail.com")
                .password("hashedPassword")
                .role("user")
                .build();
        CGUserGameData testGameData = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(0)
                .questItems(new ArrayList<>(List.of(new QuestItem("testQuestId", Date.from(Instant.now())))))
                .build();

        CGUserRepository cgUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(cgUserRepository.findById("testUserId")).thenReturn(Optional.of(testUser));
        CGUserGameDataRespository cgUserGameDataRespository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(cgUserGameDataRespository.findByUserId("testUserId")).thenReturn(Optional.of(testGameData));
        QuestRepository questRepository = Mockito.mock(QuestRepository.class);

        CGUserGameDataService cgUserGameDataService = new CGUserGameDataService(cgUserGameDataRespository,cgUserRepository,questRepository);

        CGUserGameDataDTO expected = new CGUserGameDataDTO("testusername", 0);

        //When
        CGUserGameDataDTO actual = cgUserGameDataService.getScore(testUser.getId());

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldAddPointsForResolvedQuest(){
        //Given
        CGUser testUser = CGUser.builder()
                .id("testUserId")
                .username("testusername")
                .email("test@mail.com")
                .password("hashedPassword")
                .role("user")
                .build();
        CGUserGameData testGameData = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(300)
                .questItems(new ArrayList<>(List.of(new QuestItem("testQuestId", Date.from(Instant.now())))))
                .build();

        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();

        CGUserRepository cgUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(cgUserRepository.findById("testUserId")).thenReturn(Optional.of(testUser));
        CGUserGameDataRespository cgUserGameDataRespository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(cgUserGameDataRespository.findByUserId("testUserId")).thenReturn(Optional.of(testGameData));
        QuestRepository questRepository = Mockito.mock(QuestRepository.class);

        CGUserGameDataService cgUserGameDataService = new CGUserGameDataService(cgUserGameDataRespository,cgUserRepository,questRepository);

        CGUserGameData expected = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(600)
                .questItems(new ArrayList<>(List.of(new QuestItem("testQuestId", Date.from(Instant.now())))))
                .build();

        //When
        cgUserGameDataService.scoreForQuest(testUser.getId(), testQuest.getScoreMultiplier(), testQuest.getKioskGooglePlacesIds().length);

        //Then
        Mockito.verify(cgUserGameDataRespository).save(Mockito.argThat(gd ->
                Objects.equals(gd.getId(), expected.getId()) &&
                Objects.equals(gd.getScore(), expected.getScore()) &&
                ((gd.getQuestItems().stream().findFirst().get().getTimestamp().toInstant().toEpochMilli() - expected.getQuestItems().stream().findFirst().get().getTimestamp().toInstant().toEpochMilli()) < 1000)));

    }

    @Test
    void shouldRefreshQuestItemStatus(){
        //Given

        QuestItem questItem1 = new QuestItem("testQuestItemId1", "testQuestId1", Date.from(Instant.now().minus(24, ChronoUnit.HOURS)), QuestStatus.STARTED);
        QuestItem questItem2 = new QuestItem("testQuestItemId1","testQuestId2", Date.from(Instant.now().minus(24, ChronoUnit.MINUTES)), QuestStatus.STARTED);

        CGUserGameData testGameData = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(300)
                .questItems(new ArrayList<>(List.of(questItem1,questItem2)))
                .build();

        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest1 = Quest.builder()
                .id("testQuestId1")
                .name("testName1")
                .description("Dies ist der TestNameQuest1!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();
        Quest testQuest2 = Quest.builder()
                .id("testQuestId2")
                .name("testName2")
                .description("Dies ist der TestNameQuest2!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();


        CGUserRepository cgUserRepository = Mockito.mock(CGUserRepository.class);
        CGUserGameDataRespository cgUserGameDataRespository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(cgUserGameDataRespository.findByUserId("testUserId")).thenReturn(Optional.of(testGameData));
        QuestRepository questRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(questRepository.findById("testQuestId1")).thenReturn(Optional.of(testQuest1));
        Mockito.when(questRepository.findById("testQuestId2")).thenReturn(Optional.of(testQuest2));

        CGUserGameDataService cgUserGameDataService = new CGUserGameDataService(cgUserGameDataRespository,cgUserRepository,questRepository);

        QuestItem expectedQuestItem1 = new QuestItem("testQuestItemId1", "testQuestId1", Date.from(Instant.now().minus(24, ChronoUnit.HOURS)), QuestStatus.EXPIRED);

        CGUserGameData expected = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(600)
                .questItems(new ArrayList<>(List.of(expectedQuestItem1,questItem2)))
                .build();

        //When
        cgUserGameDataService.refreshQuestItemsStatus(testGameData.getUserId());

        //Then
        Mockito.verify(cgUserGameDataRespository).save(Mockito.argThat(gd ->
                Objects.equals(gd.getId(), expected.getId()) &&
                        Objects.equals(gd.getQuestItems().stream()
                                        .filter(qi -> qi.getQuestId()
                                                .equals("testQuestId1"))
                                        .toList().stream().findFirst().get()
                                        .getQuestStatus(),
                                expected.getQuestItems().stream()
                                        .filter(qi -> qi.getQuestId().equals("testQuestId1"))
                                        .toList().stream().findFirst().get()
                                        .getQuestStatus()) &&
                        Objects.equals(
                                gd.getQuestItems().stream()
                                        .filter(qi -> qi.getQuestId()
                                        .equals("testQuestId2"))
                                        .toList().stream().findFirst().get()
                                        .getQuestStatus(),
                                expected.getQuestItems().stream()
                                        .filter(qi -> qi.getQuestId().equals("testQuestId2"))
                                        .toList().stream().findFirst().get()
                                        .getQuestStatus())));

    }

    @Test
    void shouldReturnNegativeMinutesLeftForQuest() {

        //Given
        QuestItem questItem1 = new QuestItem("testQuestItemId1", "testQuestId1", Date.from(Instant.now().minus(24, ChronoUnit.HOURS)), QuestStatus.STARTED);

        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest1 = Quest.builder()
                .id("testQuestId1")
                .name("testName1")
                .description("Dies ist der TestNameQuest1!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();

        QuestRepository questRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(questRepository.findById("testQuestId1")).thenReturn(Optional.of(testQuest1));

        CGUserRepository cgUserRepository = Mockito.mock(CGUserRepository.class);
        CGUserGameDataRespository cgUserGameDataRespository = Mockito.mock(CGUserGameDataRespository.class);

        CGUserGameDataService cgUserGameDataService = new CGUserGameDataService(cgUserGameDataRespository,cgUserRepository,questRepository);

        //When
        int actual = cgUserGameDataService.checkMinutesLeft(questItem1);

        //Then
        Assertions.assertThat(actual).isLessThan(0);

    }

    @Test
    void shouldReturnPositiveMinutesLeftForQuest() {

        //Given
        QuestItem questItem1 = new QuestItem("testQuestItemId1", "testQuestId1", Date.from(Instant.now().minus(24, ChronoUnit.MINUTES)), QuestStatus.STARTED);

        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest1 = Quest.builder()
                .id("testQuestId1")
                .name("testName1")
                .description("Dies ist der TestNameQuest1!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();

        QuestRepository questRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(questRepository.findById("testQuestId1")).thenReturn(Optional.of(testQuest1));

        CGUserRepository cgUserRepository = Mockito.mock(CGUserRepository.class);
        CGUserGameDataRespository cgUserGameDataRespository = Mockito.mock(CGUserGameDataRespository.class);

        CGUserGameDataService cgUserGameDataService = new CGUserGameDataService(cgUserGameDataRespository,cgUserRepository,questRepository);

        //When
        int actual = cgUserGameDataService.checkMinutesLeft(questItem1);

        //Then
        Assertions.assertThat(actual).isGreaterThan(0);

    }

    @Test
    void shouldCallGameDataRepositoryToSaveInput(){
        //Given
        QuestItem questItem1 = new QuestItem("testQuestItemId1", "testQuestId1", Date.from(Instant.now().minus(24, ChronoUnit.HOURS)), QuestStatus.STARTED);
        QuestItem questItem2 = new QuestItem("testQuestItemId1","testQuestId2", Date.from(Instant.now().minus(24, ChronoUnit.MINUTES)), QuestStatus.STARTED);

        CGUserGameData testGameData = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(300)
                .questItems(new ArrayList<>(List.of(questItem1,questItem2)))
                .build();

        QuestRepository questRepository = Mockito.mock(QuestRepository.class);
        CGUserRepository cgUserRepository = Mockito.mock(CGUserRepository.class);
        CGUserGameDataRespository cgUserGameDataRespository = Mockito.mock(CGUserGameDataRespository.class);

        CGUserGameDataService cgUserGameDataService = new CGUserGameDataService(cgUserGameDataRespository,cgUserRepository,questRepository);

        //When
        cgUserGameDataService.save(testGameData);

        //Then
        Mockito.verify(cgUserGameDataRespository).save(testGameData);

    }

}

