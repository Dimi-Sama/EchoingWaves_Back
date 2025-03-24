package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "box")
@Data
@EqualsAndHashCode(exclude = "boxContent")
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nom;
    private Boolean hidden;

    @JsonIgnore
    @OneToMany(mappedBy = "box")
    private Set<Tirage> tirages;

    @OneToOne
    @JoinColumn(name = "id_box_content")
    private BoxContent boxContent;
} 