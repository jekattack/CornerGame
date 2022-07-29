package com.github.jekattack.cornergame.game.visits;

import com.github.jekattack.cornergame.game.gamedata.CGUserGameData;

public interface VisitObserver {
    void onVisitCreated(Visit visit, CGUserGameData gameData);
}
