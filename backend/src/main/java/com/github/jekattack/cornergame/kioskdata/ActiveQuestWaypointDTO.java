package com.github.jekattack.cornergame.kioskdata;

import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveQuestWaypointDTO {
    ActiveQuestWaypointDTO(KioskLocationCoordinates location){
        this.location = location;
    }
    private KioskLocationCoordinates location;
    private boolean stopover = true;
}
