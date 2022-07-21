package com.github.jekattack.cornergame.game.quests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "started-quest")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartedQuest {
    @Id
    private String id;
    private String questId;
    private Date timestamp;

    public StartedQuest(String questId, Date timestamp){
        this.questId = questId;
        this.timestamp = timestamp;
    }
}
