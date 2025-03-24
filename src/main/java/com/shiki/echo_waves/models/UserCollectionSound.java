package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
@Entity
@Table(name = "user_collection_sound")
@Data
@EqualsAndHashCode(exclude = {"collection", "sound"})
public class UserCollectionSound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private UsersCollection collection;

    @ManyToOne
    @JoinColumn(name = "sound_id")
    private Sound sound;

    private Integer quantity;
} 