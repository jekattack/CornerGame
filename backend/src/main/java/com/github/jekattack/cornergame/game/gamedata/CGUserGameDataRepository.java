package com.github.jekattack.cornergame.game.gamedata;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface CGUserGameDataRepository extends MongoRepository<CGUserGameData, String> {

    Optional<CGUserGameData> findByUserId(String userId);
    ArrayList<CGUserGameData> findTop10ByOrderByScoreDesc();

    Optional<CGUserGameData> getByUserId(String userId);
}
