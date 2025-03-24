package com.shiki.echo_waves.dto;

import com.shiki.echo_waves.models.Sound;
import lombok.Data;

@Data
public class TirageResultDTO {
    private Sound sound;
    private String message;
    private String rarete;
    private Boolean isNew;
} 