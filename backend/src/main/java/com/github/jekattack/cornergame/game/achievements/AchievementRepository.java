package com.github.jekattack.cornergame.game.achievements;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface AchievementRepository extends MongoRepository<Achievement, String> {
    List<Achievement> findAllByQuestsFinished(int questsFinished);

    List<Achievement> findAllByQuestsStarted(int questsStarted);

    List<Achievement> findAllByVisitsCreated(int visitsCreated);

    List<Achievement> findAllByKiosksVisited(int kiosksVisited);
}
