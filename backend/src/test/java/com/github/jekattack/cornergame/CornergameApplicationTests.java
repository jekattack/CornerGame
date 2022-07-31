package com.github.jekattack.cornergame;

import com.github.jekattack.cornergame.game.UserLocation;
import com.github.jekattack.cornergame.game.UserLocationCoordinates;
import com.github.jekattack.cornergame.game.achievements.Achievement;
import com.github.jekattack.cornergame.game.achievements.AchievementRequirements;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataDTO;
import com.github.jekattack.cornergame.game.quests.ActiveQuestDTO;
import com.github.jekattack.cornergame.game.quests.Quest;
import com.github.jekattack.cornergame.game.visits.VisitCreationData;
import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskResponseData;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocation;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import com.github.jekattack.cornergame.placesapi.Coordinates;
import com.github.jekattack.cornergame.userdata.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CornergameApplicationTests {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CGUserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private Coordinates coordinates;

    UserCreationData adminUserCreationData = UserCreationData.builder()
            .username("admin")
            .email("admin@mail.com")
            .password("admin")
            .passwordAgain("admin")
            .build();

    UserCreationData failedUserUserCreationData1 = UserCreationData.builder()
            .username(" ")
            .email("user@mail.com")
            .password("user")
            .passwordAgain("user")
            .build();

    UserCreationData failedUserUserCreationData2 = UserCreationData.builder()
            .username("Lasse")
            .email("user@mail.com")
            .password("Essal")
            .passwordAgain("Lasse")
            .build();

    UserCreationData userUserCreationData = UserCreationData.builder()
            .username("user")
            .email("user@mail.com")
            .password("user")
            .passwordAgain("user")
            .build();

    KioskLocationCoordinates kioskLocationCoordinates1 = new KioskLocationCoordinates(53.5622178, 9.9635319);
    KioskLocationCoordinates kioskLocationCoordinates2 = new KioskLocationCoordinates(53.5623225, 9.964518399999999);
    KioskLocationCoordinates kioskLocationCoordinates3 = new KioskLocationCoordinates(53.560944, 9.9673876);
    KioskLocationCoordinates kioskLocationCoordinates4 = new KioskLocationCoordinates(53.5627212, 9.960809399999999);

    KioskLocation kioskLocation1 = new KioskLocation(kioskLocationCoordinates1);
    KioskLocation kioskLocation2 = new KioskLocation(kioskLocationCoordinates2);
    KioskLocation kioskLocation3 = new KioskLocation(kioskLocationCoordinates3);
    KioskLocation kioskLocation4 = new KioskLocation(kioskLocationCoordinates4);

    Kiosk kiosk1 = Kiosk.builder()
            .googlePlacesId("ChIJU9swXUKPsUcR0UVv2ZMG7D0")
            .name("Susannen Kiosk")
            .kioskAddress("Susannenstraße 13, Hamburg")
            .kioskLocation(kioskLocation1)
            .build();
    Kiosk kiosk2 = Kiosk.builder()
            .googlePlacesId("ChIJjcRgP0KPsUcRdlSHHHaaXRo")
            .name("Kiosk 26")
            .kioskAddress("Bartelsstraße 26, Hamburg")
            .kioskLocation(kioskLocation2)
            .build();
    Kiosk kiosk3 = Kiosk.builder()
            .googlePlacesId("ChIJOwe11LGPsUcRg02MXsSNqY8")
            .name("Aylin Kiosk")
            .kioskAddress("Sternstraße 90, Hamburg")
            .kioskLocation(kioskLocation3)
            .build();
    Kiosk kiosk4 = Kiosk.builder()
            .googlePlacesId("ChIJkQ74wUKPsUcRA-7sExhQ4g8")
            .name("Cayan Kiosk")
            .kioskAddress("Schulterblatt 83, Hamburg")
            .kioskLocation(kioskLocation4)
            .build();

    Kiosk[] kiosksResponse1 = {kiosk1, kiosk2};
    Kiosk[] kiosksResponse2 = {kiosk3};
    Kiosk[] kiosksResponse3 = {kiosk4};

    KioskResponseData response1 = new KioskResponseData(kiosksResponse1, null);
    KioskResponseData response2 = new KioskResponseData(kiosksResponse2, "testNextPageToken");
    KioskResponseData response3 = new KioskResponseData(kiosksResponse3, null);


    Achievement duplicateAchievement = Achievement.builder()
            .name("Falscher Fünfer")
            .description("Ich versuche mich reinzumogeln")
            .requirements(new AchievementRequirements(1,0,0,0))
            .build();
    Achievement achievement1 = Achievement.builder()
            .name("Hallo, ich bin der Neue!")
            .description("Erster Kioskbesuch.")
            .requirements(new AchievementRequirements(1,0,0,0))
            .build();
    Achievement achievement2 = Achievement.builder()
            .name("Schön hier")
            .description("Erster Kioskbesuch.")
            .requirements(new AchievementRequirements(0,0,0,1))
            .build();
    Achievement achievement3 = Achievement.builder()
            .name("On Tour")
            .description("Ersten Quest gestartet.")
            .requirements(new AchievementRequirements(0,1,0,0))
            .build();
    Achievement achievement4 = Achievement.builder()
            .name("So wirds gemacht!")
            .description("Ersten Quest abgeschlossen.")
            .requirements(new AchievementRequirements(0,0,1,0))
            .build();

    String[] quest1Kiosks = {kiosk4.getGooglePlacesId(), kiosk3.getGooglePlacesId()};

    Quest quest1 = Quest.builder()
            .name("Cayan und Aylin")
            .description("Der Erfindungsort des Cornergame und einfach ein zweiter Kiosk.")
            .durationInMinutes(30)
            .kioskGooglePlacesIds(quest1Kiosks)
            .scoreMultiplier(3)
            .build();

    String[] quest2Kiosks = {kiosk2.getGooglePlacesId(), kiosk1.getGooglePlacesId()};

    Quest quest2 = Quest.builder()
            .name("Bartels und Susanne")
            .description("Die beiden sind nichts besonderes.")
            .durationInMinutes(30)
            .kioskGooglePlacesIds(quest2Kiosks)
            .scoreMultiplier(2)
            .build();

    VisitCreationData visitCreationData1 = new VisitCreationData(new UserLocation(new UserLocationCoordinates(53.562296522492076, 9.963459293671585)), kiosk1.getGooglePlacesId());
    VisitCreationData failedVisitCreationData1 = new VisitCreationData(new UserLocation(new UserLocationCoordinates(53.564, 9.965)), kiosk1.getGooglePlacesId());
    VisitCreationData visitCreationData2 = new VisitCreationData(new UserLocation(new UserLocationCoordinates(53.56274688744848, 9.96101875527712)), kiosk4.getGooglePlacesId());
    VisitCreationData visitCreationData3 = new VisitCreationData(new UserLocation(new UserLocationCoordinates(53.56098542125737, 9.96735762197416)), kiosk3.getGooglePlacesId());



    @BeforeEach
    void setup(){
        Mockito.when(restTemplate.getForEntity("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + "53.1" + "," + "9.1" + "&radius=3000&keyword=kiosk&key=" + "testkey", KioskResponseData.class)).thenReturn(new ResponseEntity<>(response1, HttpStatus.OK));
        Mockito.when(restTemplate.getForEntity("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + "53.2" + "," + "9.2" + "&radius=3000&keyword=kiosk&key=" + "testkey", KioskResponseData.class)).thenReturn(new ResponseEntity<>(response2, HttpStatus.OK));
        Mockito.when(restTemplate.getForEntity("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + "53.2" + "," + "9.2" + "&radius=3000&keyword=kiosk&key=" + "testkey" + "&pagetoken=" + "testNextPageToken", KioskResponseData.class)).thenReturn(new ResponseEntity<>(response3, HttpStatus.OK));
        Mockito.when(coordinates.getCoordinates()).thenReturn(new String[][]{
                {"53.1", "9.1"},
                {"53.2", "9.2"}
        });
    }

    void setUserAsAdmin(String username){
        CGUser user = userRepository.findByUsername(username).orElseThrow();
        user.setRole("admin");
        userRepository.save(user);
    }
    HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    @Test
    void cornerGameIntegrationTest(){

        //User registrieren
        //UserController
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

        String tokenAdmin = adminLoginResponse.getBody().getToken();

        //Kiosks laden
        //PlacesApiController
        ResponseEntity<KioskResponseData[]> placesApiServiceResponse = testRestTemplate.exchange(
                "/api/placesapi/loadAllKiosks",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenAdmin)),
                KioskResponseData[].class
        );

        Assertions.assertThat(placesApiServiceResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(placesApiServiceResponse.getBody()).containsExactly(response1, response2, response3);

        //Achievements erstellen
        //AchievementController
        ResponseEntity<Achievement> achievementPostResponse1 = testRestTemplate.exchange(
                "/api/achievements/add",
                HttpMethod.POST,
                new HttpEntity<>(achievement1, createHeaders(tokenAdmin)),
                Achievement.class
        );

        Assertions.assertThat(achievementPostResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(achievementPostResponse1.getBody().getId()).isNotEmpty();
        Assertions.assertThat(achievementPostResponse1.getBody().getName()).isEqualTo(achievement1.getName());
        Assertions.assertThat(achievementPostResponse1.getBody().getDescription()).isEqualTo(achievement1.getDescription());
        Assertions.assertThat(achievementPostResponse1.getBody().getRequirements()).isEqualTo(achievement1.getRequirements());

        ResponseEntity<Achievement> achievementPostResponse2 = testRestTemplate.exchange(
                "/api/achievements/add",
                HttpMethod.POST,
                new HttpEntity<>(achievement2, createHeaders(tokenAdmin)),
                Achievement.class
        );

        Assertions.assertThat(achievementPostResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(achievementPostResponse2.getBody().getId()).isNotEmpty();
        Assertions.assertThat(achievementPostResponse2.getBody().getName()).isEqualTo(achievement2.getName());
        Assertions.assertThat(achievementPostResponse2.getBody().getDescription()).isEqualTo(achievement2.getDescription());
        Assertions.assertThat(achievementPostResponse2.getBody().getRequirements()).isEqualTo(achievement2.getRequirements());

        ResponseEntity<Achievement> achievementPostResponse3 = testRestTemplate.exchange(
                "/api/achievements/add",
                HttpMethod.POST,
                new HttpEntity<>(achievement3, createHeaders(tokenAdmin)),
                Achievement.class
        );

        Assertions.assertThat(achievementPostResponse3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(achievementPostResponse3.getBody().getId()).isNotEmpty();
        Assertions.assertThat(achievementPostResponse3.getBody().getName()).isEqualTo(achievement3.getName());
        Assertions.assertThat(achievementPostResponse3.getBody().getDescription()).isEqualTo(achievement3.getDescription());
        Assertions.assertThat(achievementPostResponse3.getBody().getRequirements()).isEqualTo(achievement3.getRequirements());

        ResponseEntity<Achievement> achievementPostResponse4 = testRestTemplate.exchange(
                "/api/achievements/add",
                HttpMethod.POST,
                new HttpEntity<>(achievement4, createHeaders(tokenAdmin)),
                Achievement.class
        );

        Assertions.assertThat(achievementPostResponse4.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(achievementPostResponse4.getBody().getId()).isNotEmpty();
        Assertions.assertThat(achievementPostResponse4.getBody().getName()).isEqualTo(achievement4.getName());
        Assertions.assertThat(achievementPostResponse4.getBody().getDescription()).isEqualTo(achievement4.getDescription());
        Assertions.assertThat(achievementPostResponse4.getBody().getRequirements()).isEqualTo(achievement4.getRequirements());


        //Achievement erstellen schlägt fehl: Achievement-Wert schon vergeben
        //AchievementController
        ResponseEntity<Achievement> failedAchievementResponse = testRestTemplate.exchange(
                "/api/achievements/add",
                HttpMethod.POST,
                new HttpEntity<>(duplicateAchievement, createHeaders(tokenAdmin)),
                Achievement.class
        );

        Assertions.assertThat(failedAchievementResponse.getStatusCodeValue()).isEqualTo(500);

        //Quests erstellen
        //QuestController
        ResponseEntity<Quest> questResponse1 = testRestTemplate.exchange(
                "/api/quests/add",
                HttpMethod.POST,
                new HttpEntity<>(quest1, createHeaders(tokenAdmin)),
                Quest.class
        );

        Assertions.assertThat(questResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(questResponse1.getBody().getId()).isNotEmpty();
        Assertions.assertThat(questResponse1.getBody().getName()).isEqualTo(quest1.getName());
        Assertions.assertThat(questResponse1.getBody().getDescription()).isEqualTo(quest1.getDescription());
        Assertions.assertThat(questResponse1.getBody().getKioskGooglePlacesIds()).isEqualTo(quest1.getKioskGooglePlacesIds());
        Assertions.assertThat(questResponse1.getBody().getDurationInMinutes()).isEqualTo(quest1.getDurationInMinutes());
        Assertions.assertThat(questResponse1.getBody().getScoreMultiplier()).isEqualTo(quest1.getScoreMultiplier());

        quest1.setId(questResponse1.getBody().getId());

        ResponseEntity<Quest> questResponse2 = testRestTemplate.exchange(
                "/api/quests/add",
                HttpMethod.POST,
                new HttpEntity<>(quest2, createHeaders(tokenAdmin)),
                Quest.class
        );

        Assertions.assertThat(questResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(questResponse2.getBody().getId()).isNotEmpty();
        Assertions.assertThat(questResponse2.getBody().getName()).isEqualTo(quest2.getName());
        Assertions.assertThat(questResponse2.getBody().getDescription()).isEqualTo(quest2.getDescription());
        Assertions.assertThat(questResponse2.getBody().getKioskGooglePlacesIds()).isEqualTo(quest2.getKioskGooglePlacesIds());
        Assertions.assertThat(questResponse2.getBody().getDurationInMinutes()).isEqualTo(quest2.getDurationInMinutes());
        Assertions.assertThat(questResponse2.getBody().getScoreMultiplier()).isEqualTo(quest2.getScoreMultiplier());

        quest2.setId(questResponse2.getBody().getId());

        //User registrieren failed: Username blank
        //UserController
        ResponseEntity<Void> failedUserCreationResponse1 = testRestTemplate.postForEntity("/api/user/register", failedUserUserCreationData1, Void.class);

        Assertions.assertThat(failedUserCreationResponse1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        //User registrieren failed: Passwörter matchen nicht
        //UserController
        ResponseEntity<Void> failedUserCreationResponse2 = testRestTemplate.postForEntity("/api/user/register", failedUserUserCreationData2, Void.class);

        Assertions.assertThat(failedUserCreationResponse2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        //User registrieren (User)
        //UserController
        ResponseEntity<Void> userCreationResponse = testRestTemplate.postForEntity("/api/user/register", userUserCreationData, Void.class);

        Assertions.assertThat(userCreationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //Login User
        //LoginController
        LoginData userLoginData = new LoginData("user", "user");
        ResponseEntity<LoginResponse> userLoginResponse = testRestTemplate.postForEntity("/api/login", userLoginData, LoginResponse.class);

        Assertions.assertThat(userLoginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(userLoginResponse.getBody().getToken()).isNotEmpty();

        String tokenUser = userLoginResponse.getBody().getToken();

        //Kiosks laden unterbunden, weil keine Admin-Rechte
        //PlacesApiController
//        ResponseEntity<KioskResponseData[]> failedPlacesApiServiceResponse = testRestTemplate.exchange(
//                "/api/placesapi/loadAllKiosks",
//                HttpMethod.GET,
//                new HttpEntity<>(createHeaders(tokenUser)),
//                KioskResponseData[].class
//        );
//
//        Assertions.assertThat(failedPlacesApiServiceResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        //Achievements erstellen unterbunden, weil keine Admin-Rechte
        //AchievementController
        ResponseEntity<Achievement> failedAchievementPostResponse1 = testRestTemplate.exchange(
                "/api/achievements/add",
                HttpMethod.POST,
                new HttpEntity<>(achievement1, createHeaders(tokenUser)),
                Achievement.class
        );

        Assertions.assertThat(failedAchievementPostResponse1.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        //Visit anlegen
        //VisitController
        ResponseEntity<Void> visitResponse1 = testRestTemplate.exchange(
                "/api/visits/add",
                HttpMethod.POST,
                new HttpEntity<>(visitCreationData1, createHeaders(tokenUser)),
                Void.class
        );

        Assertions.assertThat(visitResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //Visit failed, weil Standort nicht passt
        //VisitController
        ResponseEntity<Void> failedVisitResponse1 = testRestTemplate.exchange(
                "/api/visits/add",
                HttpMethod.POST,
                new HttpEntity<>(failedVisitCreationData1, createHeaders(tokenUser)),
                Void.class
        );

        Assertions.assertThat(failedVisitResponse1.getStatusCodeValue()).isEqualTo(500);

        //Visit failed, weil schon innerhalb der letzten 24h besucht
        //VisitController
        ResponseEntity<Void> failedVisitResponse2 = testRestTemplate.exchange(
                "/api/visits/add",
                HttpMethod.POST,
                new HttpEntity<>(visitCreationData1, createHeaders(tokenUser)),
                Void.class
        );

        Assertions.assertThat(failedVisitResponse1.getStatusCodeValue()).isEqualTo(500);

        //Score abrufen
        //GameDataController
        ResponseEntity<CGUserGameDataDTO> scoreResponse1 = testRestTemplate.exchange(
                "/api/gamedata/score",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                CGUserGameDataDTO.class
        );

        Assertions.assertThat(scoreResponse1.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(scoreResponse1.getBody().getScore()).isEqualTo(100);

        //Aktive Quests abrufen (noch leer)
        //GameDataController
        ResponseEntity<ActiveQuestDTO[]> activeQuestResponse1 = testRestTemplate.exchange(
                "/api/gamedata/quests/active",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                ActiveQuestDTO[].class
        );

        Assertions.assertThat(activeQuestResponse1.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(activeQuestResponse1.getBody().length).isEqualTo(0);

        //Quest starten, um ihn abzubrechen
        //QuestController
        ResponseEntity<String> startQuest2Response = testRestTemplate.exchange(
                "/api/quests/start",
                HttpMethod.POST,
                new HttpEntity<>(quest2.getId(), createHeaders(tokenUser)),
                String.class
        );

        Assertions.assertThat(startQuest2Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(startQuest2Response.getBody()).isEqualTo("Quest " + quest2.getName() + " gestartet!");

        //Quest abbrechen
        //QuestController
        ResponseEntity<String> cancelQuest2Response = testRestTemplate.exchange(
                "/api/quests/cancel",
                HttpMethod.DELETE,
                new HttpEntity<>(quest2.getId(), createHeaders(tokenUser)),
                String.class
        );

        Assertions.assertThat(cancelQuest2Response.getStatusCode()).isEqualTo(HttpStatus.GONE);
        Assertions.assertThat(cancelQuest2Response.getBody()).isEqualTo("Quest " + quest2.getName() + " abgebrochen.");

        //Quest abbrechen abgelehnt
        //QuestController
        ResponseEntity<String> failedCancelQuest2Response = testRestTemplate.exchange(
                "/api/quests/cancel",
                HttpMethod.DELETE,
                new HttpEntity<>(quest2.getId(), createHeaders(tokenUser)),
                String.class
        );

        Assertions.assertThat(failedCancelQuest2Response.getStatusCode()).isEqualTo(HttpStatus.GONE);
        Assertions.assertThat(failedCancelQuest2Response.getBody()).isEqualTo("Quest nicht gestartet oder schon abgelaufen.");


        //Quest starten
        //QuestController
        ResponseEntity<String> startQuest1Response = testRestTemplate.exchange(
                "/api/quests/start",
                HttpMethod.POST,
                new HttpEntity<>(quest1.getId(), createHeaders(tokenUser)),
                String.class
        );

        Assertions.assertThat(startQuest1Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(startQuest1Response.getBody()).isEqualTo("Quest " + quest1.getName() + " gestartet!");

        //Quest starten abgelehnt
        //QuestController
        ResponseEntity<String> failedStartQuest1Response = testRestTemplate.exchange(
                "/api/quests/start",
                HttpMethod.POST,
                new HttpEntity<>(quest1.getId(), createHeaders(tokenUser)),
                String.class
        );

        Assertions.assertThat(failedStartQuest1Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(failedStartQuest1Response.getBody()).isEqualTo("Quest wurde bereits gestartet.");

        //Aktive Quests abrufen
        //GameDataController
        ResponseEntity<ActiveQuestDTO[]> activeQuestResponse2 = testRestTemplate.exchange(
                "/api/gamedata/quests/active",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                ActiveQuestDTO[].class
        );

        Assertions.assertThat(activeQuestResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(activeQuestResponse2.getBody().length).isEqualTo(1);

        //Quest abschließen
        //VisitController
        ResponseEntity<Void> visitResponse2 = testRestTemplate.exchange(
                "/api/visits/add",
                HttpMethod.POST,
                new HttpEntity<>(visitCreationData2, createHeaders(tokenUser)),
                Void.class
        );

        Assertions.assertThat(visitResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Void> visitResponse3 = testRestTemplate.exchange(
                "/api/visits/add",
                HttpMethod.POST,
                new HttpEntity<>(visitCreationData3, createHeaders(tokenUser)),
                Void.class
        );

        Assertions.assertThat(visitResponse3.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //Aktive Quests abrufen (wieder leer)
        //GameDataController
        ResponseEntity<ActiveQuestDTO[]> activeQuestResponse3 = testRestTemplate.exchange(
                "/api/gamedata/quests/active",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                ActiveQuestDTO[].class
        );

        Assertions.assertThat(activeQuestResponse3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(activeQuestResponse3.getBody().length).isEqualTo(0);

        //Score abfragen
        //GameDataController
        ResponseEntity<CGUserGameDataDTO> scoreResponse2 = testRestTemplate.exchange(
                "/api/gamedata/score",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                CGUserGameDataDTO.class
        );

        Assertions.assertThat(scoreResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(scoreResponse2.getBody().getScore()).isEqualTo(700);

        //Highscore abfragen
        //GameDataController
        ResponseEntity<CGUserGameDataDTO[]> scoreResponse3 = testRestTemplate.exchange(
                "/api/gamedata/highscore",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                CGUserGameDataDTO[].class
        );

        Assertions.assertThat(scoreResponse3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(scoreResponse3.getBody().length).isEqualTo(2);

        //Alle Achievements abfragen
        //AchievementController
        ResponseEntity<Achievement[]> achievementResponse1 = testRestTemplate.exchange(
                "/api/achievements/",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                Achievement[].class
        );

        Assertions.assertThat(achievementResponse1.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(achievementResponse1.getBody().length).isEqualTo(4);

        achievement1.setId(achievementResponse1.getBody()[0].getId());
        achievement2.setId(achievementResponse1.getBody()[1].getId());
        achievement3.setId(achievementResponse1.getBody()[2].getId());
        achievement4.setId(achievementResponse1.getBody()[3].getId());

        //Achievements abfragen
        //GameDataController
        ResponseEntity<Achievement[]> achievementResponse2 = testRestTemplate.exchange(
                "/api/gamedata/achievements",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(tokenUser)),
                Achievement[].class
        );

        Assertions.assertThat(achievementResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(achievementResponse2.getBody().length).isEqualTo(4);
        Assertions.assertThat(achievementResponse2.getBody()).containsExactlyInAnyOrder(achievement1, achievement2, achievement3, achievement4);

    }
}

