package com.github.jekattack.cornergame.placesapi;

import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.kioskdata.KioskResponseData;
import com.github.jekattack.cornergame.kioskdata.KioskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlacesApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlacesApiService.class);
    private final KioskRepository kioskRepository;
    private final RestTemplate restTemplate;

    public List<KioskResponseData> getAllKiosksFromGoogle(String apiKey) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.556570,9.985951&radius=9000&keyword=kiosk&key=" + apiKey;

        List<KioskResponseData> responseData = new ArrayList<>();
        ResponseEntity<KioskResponseData> singleResponseData = restTemplate.getForEntity(url, KioskResponseData.class);
        responseData.add(singleResponseData.getBody());

        int counter = 0;

        while(singleResponseData.getBody().getNextPageToken()!=null){
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOGGER.error("Something interrupted the waiting period.");
            }

            String urlNextPage = url + "&pagetoken=" + singleResponseData.getBody().getNextPageToken();
            singleResponseData = restTemplate.getForEntity(urlNextPage, KioskResponseData.class);
            responseData.add(singleResponseData.getBody());

            counter++;
            System.out.println("Suchergebniscounter: " + counter);
        }

        LOGGER.info("Search process finished.");

        return responseData;
    }


    public void createManyKiosks(List<KioskResponseData> kioskResponseData){
        kioskResponseData.stream()
                .map(KioskResponseData::getResults)
                .flatMap(Arrays::stream)
                .forEach((kiosk) -> {
                    try {
                        kioskRepository.save(kiosk);
                    } catch (DuplicateKeyException e){
                        LOGGER.info("Kiosk already exists: " + kiosk.getName(), e);
                    }
                });
    }
}
