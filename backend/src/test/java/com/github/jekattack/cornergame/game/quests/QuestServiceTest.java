package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRepository;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
        CGUserGameDataRepository userGameDataRepository = Mockito.mock(CGUserGameDataRepository.class);
        VisitRepository visitRepository = Mockito.mock(VisitRepository.class);
        QuestObserver questObserver = Mockito.mock(QuestObserver.class);
        List<QuestObserver> questObservers = List.of(questObserver);

        QuestService testService = new QuestService(testQuestRepository, visitRepository, userGameDataRepository, questObservers);

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
        CGUserGameDataRepository userGameDataRepository = Mockito.mock(CGUserGameDataRepository.class);
        QuestObserver questObserver = Mockito.mock(QuestObserver.class);
        List<QuestObserver> questObservers = List.of(questObserver);

        QuestService testService = new QuestService(testQuestRepository, visitRepository, userGameDataRepository, questObservers);

        List<Quest> expected = List.of(testQuest);

        //When
        List<Quest> actual = testService.getAllQuests();

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);

    }

}