package com.shiki.echo_waves.dto;

import lombok.Data;

@Data
public class SoundProbabilityResponseDTO {
    private Integer id;
    private String soundName;
    private String soundType;
    private String soundRarity;
    private Double probability;
    
    public SoundProbabilityResponseDTO(Integer id, String soundName, String soundType, String soundRarity, Double probability) {
        this.id = id;
        this.soundName = soundName;
        this.soundType = soundType;
        this.soundRarity = soundRarity;
        this.probability = probability;
    }
} 