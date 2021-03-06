package com.github.jekattack.cornergame.game.visits;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jekattack.cornergame.game.UserLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitCreationData {
    private UserLocation userLocation;
    @JsonProperty("place_id")
    private String googlePlacesId;
}
