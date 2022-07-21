package com.github.jekattack.cornergame.game.quests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quest {

    @Id
    private String id;
    private String name;
    private String description;
    private String[] kioskIds;
    private int durationInMinutes;
    private int scoreMultiplier;
}
