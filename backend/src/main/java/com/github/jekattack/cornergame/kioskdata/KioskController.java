package com.github.jekattack.cornergame.kioskdata;

import com.github.jekattack.cornergame.model.CGErrorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/locations")
    public ResponseEntity<Object> getLocationsForQuest(@RequestBody String[] questPlacesIds){
        try{
            return ResponseEntity.ok().body(kioskService.getLocationsForQuest(questPlacesIds));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CGErrorDTO(e));
        }
    }

}
