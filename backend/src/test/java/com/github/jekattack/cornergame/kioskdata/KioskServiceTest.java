package com.github.jekattack.cornergame.kioskdata;

import com.github.jekattack.cornergame.kioskdata.details.KioskLocation;
import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;

class KioskServiceTest {

    @Test
    void shouldAddKiosk(){
        //Given
        Kiosk testKiosk = Kiosk.builder()
                .name("TestKio")
                .id("testid")
                .googlePlacesId("testplacesid")
                .kioskLocation(new KioskLocation(new KioskLocationCoordinates(33.22222, 9.33333)))
                .build();
        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);

        KioskService testKioskService = new KioskService(testKioskRepository);

        //When
        testKioskService.createKiosk(testKiosk);

        //Then
        Mockito.verify(testKioskRepository).save(testKiosk);
    }

    @Test
    void shouldReturnAllKiosks(){
        //Given
        Kiosk testKiosk1 = Kiosk.builder()
                .name("TestKio")
                .id("testid")
                .googlePlacesId("testplacesid")
                .kioskLocation(new KioskLocation(new KioskLocationCoordinates(33.22222, 9.33333)))
                .build();

        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);
        KioskService testKioskService = new KioskService(testKioskRepository);

        //When
        Mockito.when(testKioskRepository.findAll()).thenReturn(List.of(testKiosk1));

        //Then
        Assertions.assertThat(testKioskService.getAllKiosks()).isEqualTo(List.of(testKiosk1));
    }

    @Test
    void shouldSaveListOfKiosks(){
        //Given
        Kiosk testKiosk1 = Kiosk.builder()
                .name("TestKio")
                .id("testid")
                .googlePlacesId("testplacesid")
                .kioskLocation(new KioskLocation(new KioskLocationCoordinates(33.22222, 9.33333)))
                .build();

        Kiosk testKiosk2 = Kiosk.builder()
                .name("TestKio2")
                .id("testid2")
                .googlePlacesId("testplacesid2")
                .kioskLocation(new KioskLocation(new KioskLocationCoordinates(33.22221, 9.33332)))
                .build();

        List<Kiosk> TestkioskList = List.of(testKiosk1, testKiosk2);

        KioskRepository testKioskRepository = Mockito.mock(KioskRepository.class);
        KioskService testKioskService = new KioskService(testKioskRepository);

        //When
        testKioskService.createManyKiosks(TestkioskList);

        //Then
        verify(testKioskRepository).save(testKiosk1);
        verify(testKioskRepository).save(testKiosk2);
    }


}