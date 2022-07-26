package com.github.jekattack.cornergame.game.quests;

public interface QuestObserver {
    void onQuestCompleted(String userId, Quest quest);
    void onQuestStarted(String userId, Quest quest);
}
