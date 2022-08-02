package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class CGUserServiceTest {

    @Test
    void shouldCreateNewUserWithUsernameInLowerCaseAndCreateGameData(){

        //Given
        UserCreationData testUserCreationData = new UserCreationData(
                "testUsername",
                "testmail@test.de",
                "passwort123",
                "passwort123");

        CGUser expectedUser = CGUser.builder()
                .role("user")
                .username("testusername")
                .email("testmail@test.de")
                .password("hashedPassword")
                .build();

        CGUser expectedUserWithId = CGUser.builder()
                .id("testid")
                .role("user")
                .username("testusername")
                .email("testmail@test.de")
                .password("hashedPassword")
                .build();

        CGUserRepository testCGUserRepository = Mockito.mock(CGUserRepository.class);
        Mockito.when(testCGUserRepository.save(expectedUser)).thenReturn(expectedUserWithId);
        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode("passwort123")).thenReturn("hashedPassword");


        CGUserService testCGUserService = new CGUserService(testCGUserRepository, testCGUserGameDataService, passwordEncoder);


        //When
        testCGUserService.createUser(testUserCreationData);

        //Then
        Mockito.verify(testCGUserRepository).save(expectedUser);
        Mockito.verify(testCGUserGameDataService).createGameData("testid");

    }

    @Test
    void shouldNotCreateNewUserWithBlankUsername() {
        // Given
        UserCreationData userCreationData = new UserCreationData(" ", "mail@testmail.de", "password", "password");
        CGUserService userService = new CGUserService(null, null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createUser(userCreationData))
                .withMessage("Du brauchst einen Usernamen. \uD83D\uDC40");
    }

    @Test
    void shouldNotCreateNewUserWithBlankMail() {
        // Given
        UserCreationData userCreationData = new UserCreationData("testUSer", " ", "password", "password");
        CGUserService userService = new CGUserService(null, null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createUser(userCreationData))
                .withMessage("Bitte gib deine Mail-Adresse ein.");
    }

    @Test
    void shouldNotCreateNewUserWithUnmatchingPasswords() {
        // Given
        UserCreationData userCreationData = new UserCreationData("testUSer", "mail@testmail.com", "password", "apssword");
        CGUserService userService = new CGUserService(null, null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createUser(userCreationData))
                .withMessage("Passwörter unterschiedlich. \uD83D\uDE35\u200D\uD83D\uDCAB");
    }

    @Test
    void shouldGetLoggedInUser() {
        //Given
        CGUser expectedUser = CGUser.builder()
                .role("user")
                .id("testUserId")
                .username("testusername")
                .email("testmail@test.de")
                .password("hashedPassword")
                .build();

        CGUserRepository testCGUserRepository = Mockito.mock(CGUserRepository.class);
        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);


        CGUserService testCGUserService = new CGUserService(testCGUserRepository, testCGUserGameDataService, passwordEncoder);

        String expectedSearchUserId = "testUserId";
        Mockito.when(testCGUserRepository.findById(expectedSearchUserId)).thenReturn(Optional.of(expectedUser));

        //When
        testCGUserService.getUser("testUserId");

        //Then
        Mockito.verify(testCGUserRepository).findById("testUserId");
        Assertions.assertThat(testCGUserService.getUser("testUserId")).isEqualTo(expectedUser);
    }

}