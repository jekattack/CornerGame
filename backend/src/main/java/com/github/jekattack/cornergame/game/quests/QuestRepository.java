package com.github.jekattack.cornergame.game.quests;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestRepository extends MongoRepository<Quest, String> {
}
