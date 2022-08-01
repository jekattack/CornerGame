package com.github.jekattack.cornergame.game.gamedata;

public interface CGUserGameDataObserver {
    void onQuestStartedInGameData(CGUserGameData gameData);
    void onQuestCompletedInGameData(CGUserGameData gameData);
    void onVisitCreatedInGameData(CGUserGameData gameData);
}
