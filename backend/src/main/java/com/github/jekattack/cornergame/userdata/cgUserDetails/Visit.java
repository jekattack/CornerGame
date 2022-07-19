package com.github.jekattack.cornergame.userdata.cgUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "visits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visit {

    @Id
    private String id;
    private String googlePlacesId;
    private Date issuedAt;
    private enum visitType{
        GEOLOCATION,
        QRCODE
    };

}
