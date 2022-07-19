package com.github.jekattack.cornergame.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "visits")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Visit {

    @Id
    private String id;
    private String userId;
    private String googlePlacesId;
    private Date timestamp;
    private String questid;

}
