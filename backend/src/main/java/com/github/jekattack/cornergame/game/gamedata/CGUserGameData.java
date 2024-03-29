package com.github.jekattack.cornergame.game.gamedata;

import com.github.jekattack.cornergame.game.gamedata.questItem.QuestItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "gamedata")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGUserGameData {

    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;
    @Builder.Default
    private int score = 0;
    private List<String> achievementIds = new ArrayList<>();
    private List<QuestItem> questItems = new ArrayList<>();

    public CGUserGameData(String userId){
        this.userId = userId;
    }

}
