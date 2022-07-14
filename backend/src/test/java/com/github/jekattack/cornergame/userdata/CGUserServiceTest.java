package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.userdata.CGUserRepository;
import com.github.jekattack.cornergame.userdata.CGUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CGUserServiceTest {

    @Test
    void shouldCreateNewUserWithUsernameInLowerCase(){

        //Given
        CGUserRepository testCGUserRepository = Mockito.mock(CGUserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode("passwort123")).thenReturn("hashedPassword");

        UserCreationData testUserCreationData = new UserCreationData(
                "testUsername",
                "testmail@test.de",
                "passwort123",
                "passwort123");

        CGUserService testCGUserService = new CGUserService(testCGUserRepository, passwordEncoder);

        CGUser expectedUser = CGUser.builder()
                .role("user")
                .username("testusername")
                .email("testmail@test.de")
                .password("hashedPassword")
                .build();

        //When
        testCGUserService.createUser(testUserCreationData);

        //Then
        Mockito.verify(testCGUserRepository).save(expectedUser);

    }

    @Test
    void shouldNotCreateNewUserWithBlankUsername() {
        // Given
        UserCreationData userCreationData = new UserCreationData(" ", "mail@testmail.de", "password", "password");
        CGUserService userService = new CGUserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createUser(userCreationData))
                .withMessage("Registration failed: No username set");
    }

    @Test
    void shouldNotCreateNewUserWithBlankMail() {
        // Given
        UserCreationData userCreationData = new UserCreationData("testUSer", " ", "password", "password");
        CGUserService userService = new CGUserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createUser(userCreationData))
                .withMessage("Registration failed: No email set");
    }

    @Test
    void shouldNotCreateNewUserWithUnmatchingPasswords() {
        // Given
        UserCreationData userCreationData = new UserCreationData("testUSer", "mail@testmail.com", "password", "apssword");
        CGUserService userService = new CGUserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createUser(userCreationData))
                .withMessage("Password validation failed: Entered passwords don't match");
    }

    @Test
    void shouldSearchForUserByUsername() {
        //Given
        CGUser expectedUser = CGUser.builder()
                .role("user")
                .username("testusername")
                .email("testmail@test.de")
                .password("hashedPassword")
                .build();

        CGUserRepository testCGUserRepository = Mockito.mock(CGUserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);


        CGUserService testCGUserService = new CGUserService(testCGUserRepository, passwordEncoder);

        String expectedSearchUsername = "testusername";
        Mockito.when(testCGUserRepository.findByUsername(expectedSearchUsername)).thenReturn(Optional.of(expectedUser));

        //When
        testCGUserService.findByUsername("TestUserName");

        //Then
        Mockito.verify(testCGUserRepository).findByUsername("testusername");
        Assertions.assertThat(testCGUserService.findByUsername("TestUserName").orElseThrow()).isEqualTo(expectedUser);
    }

}