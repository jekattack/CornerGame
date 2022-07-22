package com.github.jekattack.cornergame.game.quests;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataRespository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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

}