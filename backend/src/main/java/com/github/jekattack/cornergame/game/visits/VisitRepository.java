package com.github.jekattack.cornergame.game.visits;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VisitRepository extends MongoRepository<Visit, String> {
    Visit[] findAllByUserId(String userId);

}
