package com.github.jekattack.cornergame.userdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationData {
    private String username;
    private String email;
    private String password;
    private String passwordAgain;
}
