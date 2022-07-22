package com.github.jekattack.cornergame.game.quests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveQuestDTO {
    private Quest quest;
    private int minutesLeft;
}
