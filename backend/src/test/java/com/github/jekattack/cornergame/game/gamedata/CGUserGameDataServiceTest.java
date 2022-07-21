package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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



}