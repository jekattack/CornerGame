package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.UserLocation;
import com.github.jekattack.cornergame.game.UserLocationCoordinates;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.quests.QuestService;
import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocation;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import com.github.jekattack.cornergame.userdata.CGUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

class VisitServiceTest {

    @Test
    void shouldNotCreateNewVisitBecauseOfInadequateCoordinates(){
        //Given
        String testUsername = "bo";
        String testGooglePlacesId = "ChIJ5Xr0R1CPsUcRwgzxHRvBnf4";
        UserLocationCoordinates testUserLocationCoordinates = new UserLocationCoordinates(52.5736689, 9.9607373);
        UserLocation testUserLocation = new UserLocation(testUserLocationCoordinates);
        VisitCreationData testVisitCreationData = new VisitCreationData(testUserLocation, testGooglePlacesId);

        VisitRepository testVisitRepository = Mockito.mock(VisitRepository.class);
        ArrayList<Visit> visitsOfTestUser = new ArrayList<>();
        Mockito.when(testVisitRepository.findAllByUserId("testid")).thenReturn(visitsOfTestUser);

        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);
        KioskLocationCoordinates testKioskLocationCoordinates = new KioskLocationCoordinates(53.5736689, 9.9607373);
        KioskLocation testKioskLocation = new KioskLocation(testKioskLocationCoordinates);
        Kiosk testKiosk = Kiosk.builder()
                .kioskLocation(testKioskLocation)
                .name("testKiosk")
                .googlePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4").build();
        Mockito.when(testKioskRepository.findByGooglePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4")).thenReturn(Optional.of(testKiosk));

        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);
        QuestService testQuestService = Mockito.mock(QuestService.class);

        VisitService testVisitService = new VisitService(testVisitRepository, testKioskRepository, testCGUserGameDataService, testQuestService);

        //Then
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> testVisitService.createVisit(testVisitCreationData, testUsername));
    }

    @Test
    void shouldCreateNewVisitAsPartOfTestQuest1(){
        //Given
        CGUser testUser = CGUser.builder()
                .id("testUserId")
                .username("testUsername")
                .role("user")
                .password("hashedPassword")
                .email("mail@test.de")
                .build();
        QuestItem questItem1 = new QuestItem("testQuestItemId1", "testQuestId1", java.sql.Date.from(Instant.now().minus(24, ChronoUnit.HOURS)), QuestStatus.STARTED);
        QuestItem questItem2 = new QuestItem("testQuestItemId2","testQuestId2", java.sql.Date.from(Instant.now().minus(24, ChronoUnit.MINUTES)), QuestStatus.STARTED);
        CGUserGameData testUserGameData = CGUserGameData.builder()
                .id("testGameDataId")
                .userId("testUserId")
                .score(300)
                .questItems(new ArrayList<>(List.of(questItem1, questItem2)))
                .build();
        KioskLocationCoordinates testKioskLocationCoordinates1 = new KioskLocationCoordinates(53, 9);
        KioskLocation testKioskLocation1 = new KioskLocation(testKioskLocationCoordinates1);
        Kiosk testKiosk1 = Kiosk.builder()
                .kioskLocation(testKioskLocation1)
                .name("testKiosk1")
                .googlePlacesId("ChIJO1UA9SqJsUcRXBMA3ct5jS8").build();
        KioskLocationCoordinates testKioskLocationCoordinates2 = new KioskLocationCoordinates(54, 10);
        KioskLocation testKioskLocation2 = new KioskLocation(testKioskLocationCoordinates2);
        Kiosk testKiosk2 = Kiosk.builder()
                .kioskLocation(testKioskLocation2)
                .name("testKiosk2")
                .googlePlacesId("ChIJAQANpGyJsUcR3pgAdCAy5Zk").build();
        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk"};
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
        Visit testVisit = Visit.builder()
                .id("testVisitId1")
                .googlePlacesId(testKiosk2.getGooglePlacesId())
                .questId("testQuestId1")
                .userId("testUserId")
                .timestamp(Date.from(Instant.now().minus(5, ChronoUnit.MINUTES))).build();
        Visit newTestVisit = Visit.builder()
                .id("testVisitId2")
                .googlePlacesId(testKiosk1.getGooglePlacesId())
                .questId("testQuestId1")
                .userId("testUserId")
                .timestamp(Date.from(Instant.now())).build();
        UserLocationCoordinates testUserLocationCoordinates = new UserLocationCoordinates(53.00001, 9.00001);
        UserLocation testUserLocation = new UserLocation(testUserLocationCoordinates);
        VisitCreationData testVisitCreationData = new VisitCreationData(testUserLocation, testKiosk1.getGooglePlacesId());


        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);
        Mockito.when(testKioskRepository.findByGooglePlacesId("ChIJO1UA9SqJsUcRXBMA3ct5jS8")).thenReturn(Optional.of(testKiosk1));

        VisitRepository testVisitRepository = Mockito.mock(VisitRepository.class);
        Mockito.when(testVisitRepository.findAllByUserId("testUserId")).thenReturn(new ArrayList<>(List.of(testVisit)));
        Mockito.when(testVisitRepository.findAllByQuestId("testQuestId1")).thenReturn(List.of(testVisit, newTestVisit));
        Mockito.when(testVisitRepository.findAllByQuestIdAndTimestampIsAfter(Mockito.eq(testQuest1.getId()), Mockito.any(Date.class))).thenReturn(List.of(testVisit, newTestVisit));

        QuestService testQuestService = Mockito.mock(QuestService.class);
        Mockito.when(testQuestService.returnActiveQuestWithKiosk(testUser.getId(), testKiosk1.getGooglePlacesId())).thenReturn(Optional.of(testQuest1));
        Mockito.when(testQuestService.checkIfQuestComplete(List.of(testVisit, newTestVisit), testQuest1.getId())).thenReturn(true);

        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);
        Mockito.when(testCGUserGameDataService.getByUserId(testUser.getId())).thenReturn(Optional.of(testUserGameData));


        VisitService testVisitService = new VisitService(testVisitRepository, testKioskRepository, testCGUserGameDataService, testQuestService);


        Visit expectedVisit = Visit.builder()
                .userId(testUser.getId())
                .googlePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4")
                .timestamp(Date.from(Instant.now()))
                .questId(testQuest1.getId())
                .build();

        //When
        String actual = testVisitService.createVisit(testVisitCreationData, testUser.getId());

        //Then
        Assertions.assertThat(actual).isEqualTo("Kiosk besucht und Quest testName1 abgeschlossen!");

        Mockito.verify(testCGUserGameDataService).scoreForQuestAndMarkAsDone(testUser.getId(), testQuest1);
//        Mockito.verify(testVisitRepository).existsByUserIdAndAndGooglePlacesIdAndTimestampAfter(Mockito.eq(testUser.getId()), Mockito.eq(testKiosk1.getGooglePlacesId()), Mockito.any());
//        Mockito.verify(testVisitRepository).findAllByQuestIdAndTimestampIsAfter(Mockito.eq(testQuest1.getId()), Mockito.any());
//        Mockito.verify(testVisitRepository).save(Mockito.argThat(actualVisit ->
//                Objects.equals(actualVisit.getUserId(), expectedVisit.getUserId()) &&
//                Objects.equals(actualVisit.getQuestId(), expectedVisit.getQuestId()) &&
//                Objects.equals(actualVisit.getGooglePlacesId(), expectedVisit.getGooglePlacesId())));
    }



}