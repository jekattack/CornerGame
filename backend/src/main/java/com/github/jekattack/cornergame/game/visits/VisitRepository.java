package com.github.jekattack.cornergame.game.visits;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface VisitRepository extends MongoRepository<Visit, String> {
    ArrayList<Visit> findAllByUserId(String userId);

}
