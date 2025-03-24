package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "sound")
@Data
@EqualsAndHashCode(exclude = {"collectionEntries", "probabilities"})
public class Sound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nom;
    private String type;
    private SoundRarety rarete;
    private String link;

    @JsonIgnore
    @OneToMany(mappedBy = "sound")
    private Set<UserCollectionSound> collectionEntries;

    @JsonIgnore
    @OneToMany(mappedBy = "sound")
    private Set<SoundProbability> probabilities;
} 