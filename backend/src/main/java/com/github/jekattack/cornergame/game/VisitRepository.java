package com.github.jekattack.cornergame.game;

import com.github.jekattack.cornergame.userdata.CGUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VisitRepository extends MongoRepository<Visit, String> {
    Visit[] findAllByUserId(String userId);

}
