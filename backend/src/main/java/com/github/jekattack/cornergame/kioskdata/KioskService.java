package com.github.jekattack.cornergame.kioskdata;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final KioskRepository kioskRepository;
    private final RestTemplate restTemplate;

    public KioskResponseData[] getAllKiosksFromGoogle(String apiKey) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.556570,9.985951&radius=9000&keyword=kiosk&key=" + apiKey;

        List<KioskResponseData> responseDataList = new ArrayList<>();

        ResponseEntity<KioskResponseData> singleResponseData = restTemplate.getForEntity(url, KioskResponseData.class);
        responseDataList.add(singleResponseData.getBody());

        System.out.println(singleResponseData.getBody());
        int counter = 0;

        while(singleResponseData.getBody().getNextPageToken()!=null && counter < 3){
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("something interrupted the waiting period");
            }

            String urlNextPage = url + "&pagetoken=" + singleResponseData.getBody().getNextPageToken();
            singleResponseData = restTemplate.getForEntity(urlNextPage, KioskResponseData.class);
            responseDataList.add(singleResponseData.getBody());

            counter++;
            System.out.println("Suchergebniscounter: " + counter);
            System.out.println(urlNextPage);
            System.out.println(singleResponseData.getBody());
            System.out.println(singleResponseData.getBody().getResults().length);
        }

        System.out.println("Suche beendet");

        KioskResponseData[] responseData = new KioskResponseData[responseDataList.size()];
        responseData = responseDataList.toArray(responseData);

        return responseData;
    }


    public void createManyKiosks(KioskResponseData[] kioskResponseData){
        List<Kiosk[]> extractedResults = Arrays.stream(kioskResponseData).map(KioskResponseData::getResults).toList();
        List<List<Kiosk>> convertedExtractedResults = extractedResults
                .stream()
                .map((kioskArray) -> Arrays.stream(kioskArray)
                .toList())
                .collect(Collectors.toList());

//        List<Kiosk> kioskList = new ArrayList<>();
//
//        for (List<Kiosk> kioskSubList : convertedExtractedResults) {
//            Stream.concat(kioskList.stream(), kioskSubList.stream()).collect(Collectors.toList());
//        }

        List<Kiosk> kioskList =
                convertedExtractedResults.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

        Kiosk testKiosk = Kiosk.builder().name("testkiosk").id("123").kioskAddress("testadress11").build();
        Kiosk testKiosk2 = Kiosk.builder().name("testkiosk").id("124").kioskAddress("testadress11").build();
        List<Kiosk> testKiosks = new ArrayList<>();
        testKiosks.add(testKiosk);
        testKiosks.add(testKiosk2);


        kioskList.forEach(kiosk -> kioskRepository.save(kiosk));
    }

    public List<Kiosk> getAllKiosks() {
        return kioskRepository.findAll();
    }
}
