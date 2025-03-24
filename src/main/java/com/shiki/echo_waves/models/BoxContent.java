package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
@Entity
@Table(name = "box_content")
@Data
@EqualsAndHashCode(exclude = "box")
public class BoxContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JsonIgnore
    @OneToOne(mappedBy = "boxContent")
    private Box box;
    
    @JsonIgnore
    @OneToMany(mappedBy = "boxContent", cascade = CascadeType.ALL)
    private Set<SoundProbability> soundProbabilities;

} 