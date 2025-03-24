package com.shiki.echo_waves.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "tirage")
@Data
public class Tirage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "id_box", nullable = false)
    private Box box;
    
    private Date date_tirage;
    private String info_tirage;
} 