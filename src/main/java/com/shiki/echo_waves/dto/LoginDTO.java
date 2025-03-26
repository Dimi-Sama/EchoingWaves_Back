package com.shiki.echo_waves.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private String pseudo;
    private String password;
    

    public LoginDTO(String pseudo, String password) {
        this.pseudo = pseudo;
        this.password = password;
    }
//red
}
