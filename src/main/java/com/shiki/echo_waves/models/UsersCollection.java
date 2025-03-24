package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "users_collection")
@Data
@EqualsAndHashCode(exclude = {"user", "collectionSounds"})
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class UsersCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "collection")
    private Set<UserCollectionSound> collectionSounds;
} 