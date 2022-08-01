package com.github.jekattack.cornergame.game.gamedata.achievements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "achievements")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  Achievement {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String description;
    @Indexed(unique = true)
    private AchievementRequirements requirements;

}
