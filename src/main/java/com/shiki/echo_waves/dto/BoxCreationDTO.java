package com.shiki.echo_waves.dto;

import lombok.Data;
import java.util.List;

@Data
public class BoxCreationDTO {
    private String nom;
    private Boolean hidden;
    private List<SoundProbabilityDTO> soundProbabilities;
}
