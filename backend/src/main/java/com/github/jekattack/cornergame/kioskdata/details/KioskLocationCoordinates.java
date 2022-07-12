package com.github.jekattack.cornergame.kioskdata.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class KioskLocationCoordinates {
    private final double lat;
    private final double lng;
}
