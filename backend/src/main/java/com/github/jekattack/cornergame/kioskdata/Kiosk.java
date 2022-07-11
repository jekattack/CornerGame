package com.github.jekattack.cornergame.kioskdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jekattack.cornergame.kioskdata.details.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "kiosks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Kiosk {

    @Id
    private String id;
    @Indexed(unique = true)
    @JsonProperty("place_id")
    private String googlePlacesId;
    private String name;
    @JsonProperty("vicinity")
    private String kioskAddress;
    @JsonProperty("geometry")
    private KioskLocation kioskLocation;

    private boolean isManaged;

    private String shopkeeperId;

    private String phone;
    private String shortDescription;
    private OpeningHours openingHours;
    private boolean hasRestroom;
    private boolean hasOutdoors;
    private boolean hasBakery;
    private boolean hasLottery;
    private Parcelshop parcelshop;

}
