package com.github.jekattack.cornergame.kioskdata.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KioskLocationCoordinates {
    private double lat;
    private double lng;
}
