package com.github.jekattack.cornergame.game.quests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestEventDTO {
    private String message;
    private Quest quest;
}
