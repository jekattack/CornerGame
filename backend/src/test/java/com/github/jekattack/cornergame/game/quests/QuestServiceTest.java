package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QuestServiceTest {

    @Test
    void shouldAddNewQuest(){
        //Given
        String[] kioskIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskIds(kioskIdsInput)
                .scoreMultiplier(2)
                .build();

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);
        CGUserGameDataRespository testCGUserGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);

        QuestService testService = new QuestService(testQuestRepository, testCGUserGameDataRepository);

        //When
        testService.addQuest(testQuest);

        //Then
        Mockito.verify(testQuestRepository).save(testQuest);
    }

    @Test
    void shouldGetAllQuestsFromDB(){
        //Given
        String[] kioskIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskIds(kioskIdsInput)
                .scoreMultiplier(2)
                .build();

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(testQuestRepository.findAll()).thenReturn(List.of(testQuest));
        CGUserGameDataRespository testCGUserGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);

        QuestService testService = new QuestService(testQuestRepository, testCGUserGameDataRepository);

        List<Quest> expected = List.of(testQuest);

        //When
        List<Quest> actual = testService.getAllQuests();

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldGetAllActiveQuestsForUser(){

        //Given
        String[] kioskIdsInput = {"ChIJO1UA9SqJsUcRXBMA3ct5jS8", "ChIJAQANpGyJsUcR3pgAdCAy5Zk", "ChIJgSutDYGJsUcR6sH-beVN7Ic"};
        Quest testQuest = Quest.builder()
                .id("TestQuestId")
                .name("testName")
                .description("Dies ist der TestNameQuest!")
                .durationInMinutes(60)
                .kioskIds(kioskIdsInput)
                .scoreMultiplier(2)
                .build();

        CGUserGameDataRespository testGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);
        CGUserGameData testGameData = CGUserGameData.builder()
                .userId("TestUserId")
                .id("TestGameDataId")
                .score(100)
                .startedQuests(new ArrayList<>(List.of(
                new StartedQuest(testQuest.getId(), Date.from(Instant.now())))
        )).build();

        Mockito.when(testGameDataRepository.findByUserId("TestUserId")).thenReturn(Optional.of(testGameData));

        QuestRepository testQuestRepository = Mockito.mock(QuestRepository.class);
        Mockito.when(testQuestRepository.findById(testQuest.getId())).thenReturn(Optional.of(testQuest));

        QuestService testQuestService = new QuestService(testQuestRepository,testGameDataRepository);

        ArrayList<ActiveQuestDTO> expected = new ArrayList<>(List.of(new ActiveQuestDTO(testQuest, 30)));

        //When
        ArrayList<ActiveQuestDTO> actual = testQuestService.getActiveQuests("TestUserId");

        //Then
        Assertions.assertThat(actual.get(0).getQuest()).isEqualTo(expected.get(0).getQuest());

    }

}