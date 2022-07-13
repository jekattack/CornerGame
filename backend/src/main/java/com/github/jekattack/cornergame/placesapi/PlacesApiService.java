package com.github.jekattack.cornergame.placesapi;

import com.github.jekattack.cornergame.kioskdata.Kiosk;
import com.github.jekattack.cornergame.kioskdata.KioskRepository;
import com.github.jekattack.cornergame.kioskdata.KioskResponseData;
import com.github.jekattack.cornergame.kioskdata.KioskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlacesApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlacesApiService.class);
    private final KioskService kioskService;
    private final RestTemplate restTemplate;
    private final String googleMapsApiKey;

    public PlacesApiService(KioskService kioskService, RestTemplate restTemplate, @Value("${app.googleMaps.key}") String googleMapsApiKey){
        this.kioskService = kioskService;
        this.restTemplate = restTemplate;
        this.googleMapsApiKey = googleMapsApiKey;
    };


    public List<KioskResponseData> getAllKiosksFromGoogle() {

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.556570,9.985951&radius=9000&keyword=kiosk&key=" + googleMapsApiKey;
        List<KioskResponseData> responseData = new ArrayList<>();

        //Send first Request to GooglePlaces-Api
        ResponseEntity<KioskResponseData> singleResponseData = restTemplate.getForEntity(url, KioskResponseData.class);
        responseData.add(singleResponseData.getBody());

        //GetRequest for next pages of initial Request
        while(singleResponseData.getBody().getNextPageToken()!=null){
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOGGER.error("Something interrupted the waiting period.");
            }
            String urlNextPage = url + "&pagetoken=" + singleResponseData.getBody().getNextPageToken();
            singleResponseData = restTemplate.getForEntity(urlNextPage, KioskResponseData.class);
            responseData.add(singleResponseData.getBody());
        }
        LOGGER.info("Search process finished.");

        //Store Results
        kioskService.createManyKiosks(convertKioskResponse(responseData));

        return responseData;
    }

    public List<Kiosk> convertKioskResponse(List<KioskResponseData> kioskResponseData){
        //Convert ResponseData for KioskController
        return kioskResponseData.stream()
                .map(KioskResponseData::getResults)
                .flatMap(Arrays::stream).toList();
    }

}
