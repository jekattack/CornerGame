package com.github.jekattack.cornergame.userdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGUser {

    CGUser(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id
    private String id;
    private String role;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private boolean validated;

    private String lastname;
    private String firstname;
    private String phone;
    private String stammkioskId;

    private int score;


}
