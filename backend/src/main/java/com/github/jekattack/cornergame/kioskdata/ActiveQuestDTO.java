package com.github.jekattack.cornergame.kioskdata;

import com.github.jekattack.cornergame.kioskdata.details.KioskLocationCoordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveQuestDTO {
    private KioskLocationCoordinates start;
    private List<ActiveQuestWaypointDTO> waypoints;
    private KioskLocationCoordinates finish;
}
