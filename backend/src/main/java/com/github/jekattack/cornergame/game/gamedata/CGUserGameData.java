package com.github.jekattack.cornergame.game.gamedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "gamedata")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGUserGameData {

    @Id
    private String id;
    private String userId;
    private int score = 0;
    private ArrayList<String> achievementIds;
    private ArrayList<String> activeQuests;

    public CGUserGameData(String userId){
        this.userId = userId;
    }

}
