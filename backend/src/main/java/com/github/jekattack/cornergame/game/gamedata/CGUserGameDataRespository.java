package com.github.jekattack.cornergame.game.gamedata;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CGUserGameDataRespository extends MongoRepository<CGUserGameData, String> {

}
