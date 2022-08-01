package com.github.jekattack.cornergame.userdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CGUserUpdateDTO {
    public CGUserUpdateDTO(String firstname, String lastname, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    private String stammkioskId;
    private String firstname;
    private String lastname;
    private String phone;
}
