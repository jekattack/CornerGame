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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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

        List<KioskResponseData> responseData = new ArrayList<>();

        //Randomly chosen points in the center of Hamburg
        String[][] coordinates = {
                {"53.56733","9.8712"},
                {"53.63049","9.88562"},
                {"53.61502","9.97557"},
                {"53.60402","10.04561"},
                {"53.58283","10.08956"},
                {"53.58731","9.94673"},
                {"53.55918","9.97489"},
                {"53.55632","10.06072"},
                {"53.5347","10.10672"},
                {"53.53743","9.98038"},
                {"53.50408","10.01952"},
                {"53.53511","9.91584"}
        };

        for(String[] coordinate : coordinates){
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ coordinate[0] +"," + coordinate[1] +"&radius=3000&keyword=kiosk&key=" + googleMapsApiKey;

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
