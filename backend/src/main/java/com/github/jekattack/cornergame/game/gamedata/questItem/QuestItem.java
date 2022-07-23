package com.github.jekattack.cornergame.game.gamedata.questItem;

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
public class QuestItem {
    @Id
    private String id;
    private String questId;
    private Date timestamp;
    @Builder.Default
    private QuestStatus questStatus = QuestStatus.STARTED;

    public QuestItem(String questId, Date timestamp){
        this.questId = questId;
        this.timestamp = timestamp;
    }
}
