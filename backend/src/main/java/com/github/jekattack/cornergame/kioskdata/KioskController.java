package com.github.jekattack.cornergame.kioskdata;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kiosk")
public class KioskController {

    private final KioskService kioskService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Kiosk> getAllKiosks(){
        return kioskService.getAllKiosks();
    }

}
