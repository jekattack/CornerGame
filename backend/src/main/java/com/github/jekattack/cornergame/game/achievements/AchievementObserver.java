package com.github.jekattack.cornergame.game.achievements;

public interface AchievementObserver {
    void onAchievementRecieved(String achievementId, String userId);
}
