package com.github.jekattack.cornergame.kioskdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KioskResponseData {

    private Kiosk[] results;
    @JsonProperty("next_page_token")
    private String nextPageToken;

}
