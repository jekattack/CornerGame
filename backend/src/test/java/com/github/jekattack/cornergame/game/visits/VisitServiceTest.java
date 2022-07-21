package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.UserLocation;
import com.github.jekattack.cornergame.game.UserLocationCoordinates;
import com.github.jekattack.cornergame.game.gamedata.CGUserGameDataService;
import com.github.jekattack.cornergame.game.visits.Visit;
import com.github.jekattack.cornergame.game.visits.VisitCreationData;
import com.github.jekattack.cornergame.game.visits.VisitRepository;
import com.github.jekattack.cornergame.game.visits.VisitService;
import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocation;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

class VisitServiceTest {

    @Test
    void shouldCreateNewVisit(){
        //Given
        String testUsername = "bo";
        String testGooglePlacesId = "ChIJ5Xr0R1CPsUcRwgzxHRvBnf4";
        UserLocationCoordinates testUserLocationCoordinates = new UserLocationCoordinates(53.5736689, 9.9607373);
        UserLocation testUserLocation = new UserLocation(testUserLocationCoordinates);
        VisitCreationData testVisitCreationData = new VisitCreationData(testUserLocation, testGooglePlacesId);

        VisitRepository testVisitRepository = Mockito.mock(VisitRepository.class);
        Visit[] visitsOfTestUser = new Visit[0];
        Mockito.when(testVisitRepository.findAllByUserId("testid")).thenReturn(visitsOfTestUser);

        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);
        KioskLocationCoordinates testKioskLocationCoordinates = new KioskLocationCoordinates(53.5736689, 9.9607373);
        KioskLocation testKioskLocation = new KioskLocation(testKioskLocationCoordinates);
        Kiosk testKiosk = Kiosk.builder().kioskLocation(testKioskLocation).name("testKiosk").googlePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4").build();
        Mockito.when(testKioskRepository.findByGooglePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4")).thenReturn(Optional.of(testKiosk));

        CGUserRepository testCGUserRepository = Mockito.mock(CGUserRepository.class);
        CGUser testUser = CGUser.builder().username("bo").id("testid").build();
        Mockito.when(testCGUserRepository.findByUsername("bo")).thenReturn(Optional.of(testUser));

        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);

        VisitService testVisitService = new VisitService(testVisitRepository, testKioskRepository, testCGUserRepository, testCGUserGameDataService);

        Visit expectedVisit = Visit.builder().userId("testid").googlePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4").timestamp(Date.from(Instant.now())).build();

        //When
        testVisitService.createVisit(testVisitCreationData, testUsername);

        //Then
        Mockito.verify(testVisitRepository).save(Mockito.argThat(v ->
                Objects.equals(v.getGooglePlacesId(), expectedVisit.getGooglePlacesId()) &&
                Objects.equals(v.getUserId(), expectedVisit.getUserId()) &&
                ((v.getTimestamp().getTime() - expectedVisit.getTimestamp().getTime()) < 1000)));
    }

    @Test
    void shouldNotCreateNewVisit(){
        //Given
        String testUsername = "bo";
        String testGooglePlacesId = "ChIJ5Xr0R1CPsUcRwgzxHRvBnf4";
        UserLocationCoordinates testUserLocationCoordinates = new UserLocationCoordinates(52.5736689, 9.9607373);
        UserLocation testUserLocation = new UserLocation(testUserLocationCoordinates);
        VisitCreationData testVisitCreationData = new VisitCreationData(testUserLocation, testGooglePlacesId);

        VisitRepository testVisitRepository = Mockito.mock(VisitRepository.class);
        Visit[] visitsOfTestUser = new Visit[0];
        Mockito.when(testVisitRepository.findAllByUserId("testid")).thenReturn(visitsOfTestUser);

        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);
        KioskLocationCoordinates testKioskLocationCoordinates = new KioskLocationCoordinates(53.5736689, 9.9607373);
        KioskLocation testKioskLocation = new KioskLocation(testKioskLocationCoordinates);
        Kiosk testKiosk = Kiosk.builder().kioskLocation(testKioskLocation).name("testKiosk").googlePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4").build();
        Mockito.when(testKioskRepository.findByGooglePlacesId("ChIJ5Xr0R1CPsUcRwgzxHRvBnf4")).thenReturn(Optional.of(testKiosk));

        CGUserRepository testCGUserRepository = Mockito.mock(CGUserRepository.class);
        CGUser testUser = CGUser.builder().username("bo").id("testid").build();
        Mockito.when(testCGUserRepository.findByUsername("bo")).thenReturn(Optional.of(testUser));

        CGUserGameDataService testCGUserGameDataService = Mockito.mock(CGUserGameDataService.class);

        VisitService testVisitService = new VisitService(testVisitRepository, testKioskRepository, testCGUserRepository, testCGUserGameDataService);

        //Then
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> testVisitService.createVisit(testVisitCreationData, testUsername));
    }

}