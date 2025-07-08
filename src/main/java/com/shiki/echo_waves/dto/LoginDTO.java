package com.shiki.echo_waves.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private Integer id;
    private String pseudo;
    private String password;
    

    public LoginDTO(Integer id, String pseudo, String password) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
    }
//reddi
}
