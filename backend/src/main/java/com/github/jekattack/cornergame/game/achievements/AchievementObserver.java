package com.github.jekattack.cornergame.game.achievements;

import java.util.List;

public interface AchievementObserver {
    void onAchievementReceived(List<String> achievementId, String userId);
}
