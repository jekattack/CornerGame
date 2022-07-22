package com.github.jekattack.cornergame.kioskdata;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kiosk")
public class KioskController {

    private final KioskService kioskService;

    public KioskController(KioskService kioskService){
        this.kioskService = kioskService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Kiosk> getAllKiosks(){
        return kioskService.getAllKiosks();
    }

}
