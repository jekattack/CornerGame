package com.github.jekattack.cornergame.placesapi.Sandbox;

import com.github.jekattack.cornergame.kioskdata.KioskResponseData;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class tsx {


    int counter = 0;
    List<KioskResponseData> responseData = new ArrayList<>();

    int latStart = 5376;
    int lngStart = 1032;
    int[] latSteps = IntStream.range(0, 34).toArray();
    int[] lngSteps = IntStream.range(0, 58).toArray();

    for(int latStep : latSteps){
        double lat = ((double) (latStart - latStep)) * 0.01;
        for(int lngStep : lngSteps){
            double lng = ((double) (lngStart - lngStep)) * 0.01;
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ lat +"," + lng +"&radius=2500&keyword=kiosk&key=" + googleMapsApiKey;

            ResponseEntity<KioskResponseData> singleResponseData = restTemplate.getForEntity(url, KioskResponseData.class);
            responseData.add(singleResponseData.getBody());

            //GetRequest for next pages of initial Request
            while(singleResponseData.getBody().getNextPageToken()!=null){
                try{
                    Thread.sleep(2000);
                    counter++;
                    System.out.println(counter + singleResponseData.getBody().getResults()[0].getName());
                } catch (InterruptedException ex) {
                    LOGGER.error("Something interrupted the waiting period.");
                }
                String urlNextPage = url + "&pagetoken=" + singleResponseData.getBody().getNextPageToken();
                singleResponseData = restTemplate.getForEntity(urlNextPage, KioskResponseData.class);
                responseData.add(singleResponseData.getBody());
            }
            LOGGER.info("Search process finished.");
        }
    }

    LOGGER.info("Search process finished.");

    //Store Results
    kioskService.createManyKiosks(convertKioskResponse(responseData));

    return responseData;



}
