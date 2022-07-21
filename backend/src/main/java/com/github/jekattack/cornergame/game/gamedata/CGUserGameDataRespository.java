package com.github.jekattack.cornergame.game.gamedata;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CGUserGameDataRespository extends MongoRepository<CGUserGameData, String> {

    Optional<CGUserGameData> findByUserId(String userId);
    ArrayList<CGUserGameData> findTop10ByOrderByScore();
}
