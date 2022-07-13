package com.github.jekattack.cornergame.kioskdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kiosk")
public class KioskController {

    private final KioskService kioskService;
    private final String googleMapsApiKey;

    public KioskController(KioskService kioskService, @Value("${app.googleMaps.key}") String googleMapsApiKey){
        this.kioskService = kioskService;
        this.googleMapsApiKey = googleMapsApiKey;
    };

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Kiosk> getAllKiosks(){
        return kioskService.getAllKiosks();
    }

}
