package com.github.jekattack.cornergame.game.visits;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends MongoRepository<Visit, String> {
    ArrayList<Visit> findAllByUserId(String userId);

    List<Visit> findAllByQuestId(String questId);
}
