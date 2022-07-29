package com.github.jekattack.cornergame;

import com.github.jekattack.cornergame.kioskdata.KioskResponseData;
import com.github.jekattack.cornergame.userdata.*;
import org.apache.catalina.User;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CornergameApplicationTests {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CGUserRepository userRepository;
    private void setUserAsAdmin(String username){
        CGUser user = userRepository.findByUsername(username).orElseThrow();
        user.setRole("admin");
        userRepository.save(user);
    }
    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    @Test
    void cornerGameIntegrationTest(){

        //User registrieren
        //UserController
        UserCreationData adminUserCreationData = UserCreationData.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("admin")
                .passwordAgain("admin")
                .build();
        ResponseEntity<Void> adminCreationResponse = testRestTemplate.postForEntity("/api/user/register", adminUserCreationData, Void.class);

        Assertions.assertThat(adminCreationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //User Role zum Admin ändern
        setUserAsAdmin("admin");

        Assertions.assertThat(userRepository.findByUsername("admin").get().getRole()).isEqualTo("admin");

        //Login User (admin)
        //LoginController
        LoginData adminLoginData = new LoginData("admin", "admin");
        ResponseEntity<LoginResponse> adminLoginResponse = testRestTemplate.postForEntity("/api/login", adminLoginData, LoginResponse.class);

        Assertions.assertThat(adminLoginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(adminLoginResponse.getBody().getToken()).isNotEmpty();

        String token = adminLoginResponse.getBody().getToken();

        //Kiosks laden
        //PlacesApiController

        //Achievements erstellen
        //AchievementController

        //Quests erstellen
        //QuestController

        //User registrieren (User)
        //UserController

        //Login User
        //LoginController

        //Visit anlegen
        //VisitController

        //Quest starten
        //QuestController

        //Quest abschließen
        //VisitController

        //Score abfragen
        //GameDataController

        //Highscore abfragen
        //GameDataController

        //Achievements abfragen
        //AchievementController

    }
}

