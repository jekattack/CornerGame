package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserGameDataService {

    private final CGUserGameDataRespository cgUserGameDataRespository;
    private final CGUserRepository cgUserRepository;

    public void createGameData(String userId) {
        cgUserGameDataRespository.save(new CGUserGameData(userId));
    }

    public void scoreForNewVisit(String userId) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId).orElseThrow();
        userGameData.setScore(userGameData.getScore() + 100);
        cgUserGameDataRespository.save(userGameData);
        log.info(userId + ": 100 Points added for new Visit");
    }

    public ArrayList<CGUserGameDataDTO> getTop10Highscore() {
        ArrayList<CGUserGameData> top10GameData = cgUserGameDataRespository.findTop10ByOrderByScore();
        ArrayList<CGUserGameDataDTO> top10GameDataExport = new ArrayList<>();
        for(CGUserGameData gameData : top10GameData){
            CGUserGameDataDTO gameDataDTO = new CGUserGameDataDTO(cgUserRepository
                    .findById(gameData.getUserId()).orElseThrow().getUsername(),
                    gameData.getScore());
            top10GameDataExport.add(gameDataDTO);
        }
        return top10GameDataExport;
    }

    public CGUserGameDataDTO getScore(String username) {
        CGUser user = cgUserRepository.findByUsername(username).orElseThrow();
        CGUserGameData gameData = cgUserGameDataRespository.findByUserId(user.getId()).orElseThrow();
        return new CGUserGameDataDTO(user.getUsername(), gameData.getScore());
    }
}
