package com.github.jekattack.cornergame.game.quests;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestRepository extends MongoRepository<Quest, String> {
    boolean existsByIdAndKioskGooglePlacesIdsContaining(@Param("id")String id, String placeid);

    Optional<Quest> findByKioskGooglePlacesIdsContaining(String googlePlacesId);
}
