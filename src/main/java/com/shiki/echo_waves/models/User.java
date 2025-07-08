package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = "usersCollection")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Le pseudo est obligatoire")
    private String pseudo;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être une adresse email valide")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    private Boolean secret_bool;
    private UserRoles role;
    private Integer points = 0;  

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Tirage> tirages;

    @OneToOne(mappedBy = "user")
    private UsersCollection usersCollection;
} 