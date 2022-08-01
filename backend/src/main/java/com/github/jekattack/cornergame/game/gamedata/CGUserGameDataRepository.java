package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.gamedata.questItem.QuestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface CGUserGameDataRepository extends MongoRepository<CGUserGameData, String> {

    Optional<CGUserGameData> findByUserId(String userId);
    ArrayList<CGUserGameData> findTop10ByOrderByScoreDesc();

//    boolean existsByIdAndQuestItems_KioskPlacesIdsContains(String userId, String questId);
//
//    boolean existsByIdAndByQuestItems_QuestId(String userId, String questId);
//
//    boolean existsByIdAndByQuestItemsQuestIdAndQuestItemsQuestStatus(String userId, String questId, QuestStatus questStatus);
}
