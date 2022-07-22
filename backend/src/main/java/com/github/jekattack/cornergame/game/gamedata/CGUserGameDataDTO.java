package com.github.jekattack.cornergame.game.gamedata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CGUserGameDataDTO {
    private String username;
    private int score;
}
