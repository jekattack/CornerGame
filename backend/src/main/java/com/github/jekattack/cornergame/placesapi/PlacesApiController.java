package com.github.jekattack.cornergame.placesapi;

import com.github.jekattack.cornergame.kioskdata.KioskResponseData;
import com.github.jekattack.cornergame.kioskdata.KioskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/placesapi")
public class PlacesApiController {

    private final PlacesApiService placesApiService;
    private final String googleMapsApiKey;

    public PlacesApiController(PlacesApiService placesApiService, @Value("${app.googleMaps.key}") String googleMapsApiKey){
        this.placesApiService = placesApiService;
        this.googleMapsApiKey = googleMapsApiKey;
    };

    @GetMapping("/admin/loadAllKiosks/L375g37574r73d")
    @ResponseStatus(HttpStatus.OK)
    public List<KioskResponseData> loadAndStoreAllKiosks(){
        return placesApiService.getAllKiosksFromGoogle(googleMapsApiKey);
    }

}
