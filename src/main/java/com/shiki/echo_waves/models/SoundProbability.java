package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sound_probability")
@Data
public class SoundProbability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "box_content_id")
    private BoxContent boxContent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sound_id")
    private Sound sound;

    private Double probability;
} 