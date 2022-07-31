package com.github.jekattack.cornergame.game.achievements;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AchievementRepository extends MongoRepository<Achievement, String> {
    List<Achievement> findAllByRequirements(AchievementRequirements achievementRequirements);
}
