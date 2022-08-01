package com.github.jekattack.cornergame.userdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CGUserPasswordDTO {
    private final String password;
    private final String passwordAgain;
}
