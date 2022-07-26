package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.visits.VisitObserver;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.*;

class QuestServiceTest {

    @Test
    void shouldAddNewQuest(){
        //Given
        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);
        CGUserGameDataService userGameDataService = Mockito.mock(CGUserGameDataService.class);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        QuestObserver questObserver = Mockito.mock(QuestObserver.class);
        List<QuestObserver> questObservers = List.of(questObserver);

        QuestService testService = new QuestService(testQuestRepository, visitRepository, userGameDataService, questObservers);

        //When
        testService.addQuest(testQuest);

        //Then
        Mockito.verify(testQuestRepository).save(testQuest);
    }

    @Test
    void shouldGetAllQuestsFromDB(){
        //Given
        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(testQuestRepository.findAll()).thenReturn(List.of(testQuest));
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);
        QuestObserver questObserver = Mockito.mock(QuestObserver.class);
        List<QuestObserver> questObservers = List.of(questObserver);

        QuestService testService = new QuestService(testQuestRepository, visitRepository, testCGUserGameDataService, questObservers);

        List<Quest> expected = List.of(testQuest);

        //When
        List<Quest> actual = testService.getAllQuests();

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldGetAllActiveQuestsForUser(){

        //Given
        String[] kioskGooglePlacesIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .id("TestQuestId")
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskGooglePlacesIds(kioskGooglePlacesIdsInput)
                .scoreMultiplier(2)
                .build();

        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        CGUserGameData testGameData = CGUserGameData.builder()
                .userId("TestUserId")
                .id("TestGameDataId")
                .score(100)
                .questItems(new ArrayList<>(List.of(
                new QuestItem("testQuestItemId", testQuest.getId(), Date.from(Instant.now()), QuestStatus.STARTED))
        )).build();

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(testQuestRepository.findById(testQuest.getId())).thenReturn(Optional.of(testQuest));

        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);
        Mockito.when(testCGUserGameDataService.refreshQuestItemsStatus("TestUserId")).thenReturn(testGameData);

        QuestObserver questObserver = Mockito.mock(QuestObserver.class);
        List<QuestObserver> questObservers = List.of(questObserver);

        QuestService testQuestService = new QuestService(testQuestRepository,visitRepository,testCGUserGameDataService, questObservers);

        ArrayList<ActiveQuestDTO> expected = new ArrayList<>(List.of(new ActiveQuestDTO(testQuest, 30)));

        //When
        ArrayList<ActiveQuestDTO> actual = testQuestService.getActiveQuests("TestUserId");

        //Then
        Assertions.assertThat(actual.get(0).getQuest()).isEqualTo(expected.get(0).getQuest());

    }

}