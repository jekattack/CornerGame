package com.github.jekattack.cornergame.game.gamedata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CGUserGameDataService {

    private final CGUserGameDataRespository cgUserGameDataRespository;

    public void createGameData(String userId) {
        cgUserGameDataRespository.save(new CGUserGameData(userId));
    }

    public void scoreForNewVisit(String userId) {
        CGUserGameData userGameData = cgUserGameDataRespository.findByUserId(userId);
        userGameData.setScore(userGameData.getScore() + 100);
        cgUserGameDataRespository.save(userGameData);
        log.info(userId + ": 100 Points added for new Visit");
    }
}
