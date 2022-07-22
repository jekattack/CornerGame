package com.github.jekattack.cornergame.game.gamedata;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
public class CGUserGameDataController {

    private final CGUserGameDataService cgUserGameDataService;

    @GetMapping("/highscore")
    @ResponseStatus(HttpStatus.OK)
    public List<CGUserGameDataDTO> getTop10Highscore(){
        return cgUserGameDataService.getTop10Highscore();
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public CGUserGameDataDTO getScore(Principal principal){
        //principal.getName() contains userId
        return cgUserGameDataService.getScore(principal.getName());
    }

}
