package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
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

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository);

        CGUserGameData expected = new CGUserGameData("testId");

        //When
        testGameDataService.createGameData(testUser.getId());

        //Then
        Mockito.verify(testGameDataRepository).save(expected);

    }

    @Test
    void shouldReturnScoreOfUser(){
        //Given
        CGUser testUser = CGUser.builder().id("testId").username("testUsername").build();
        CGUserGameData testGameData = CGUserGameData.builder().id("gameDataId").userId("testId").score(0).build();
        CGUserGameDataRespository testGameDataRepository = Mockito.mock(CGUserGameDataRespository.class);
        Mockito.when(testGameDataRepository.findByUserId("testId")).thenReturn(Optional.of(testGameData));
        CGUserRepository testUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(testUserRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository);

        //When
        CGUserGameDataDTO expected = new CGUserGameDataDTO("testUsername", 0);
        CGUserGameDataDTO actual = testGameDataService.getScore(testUser.getUsername());

        //Then
        Assertions.assertThat(expected).isEqualTo(actual);
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

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository);

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

        CGUserGameDataService testGameDataService = new CGUserGameDataService(testGameDataRepository, testUserRepository);


        //When
        ArrayList<CGUserGameDataDTO> expected = new ArrayList<>(List.of(testGameDataDTO1,testGameDataDTO2,testGameDataDTO3,testGameDataDTO4));
        ArrayList<CGUserGameDataDTO> actual = testGameDataService.getTop10Highscore();

        //Then
        Assertions.assertThat(actual).isEqualTo(expected);

    }

}