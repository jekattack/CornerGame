package com.github.jekattack.cornergame.game.gamedata.achievements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementRequirements {
    private int visitsCreated;
    private int questsStarted;
    private int questsFinished;
    private int kiosksVisited;
}
