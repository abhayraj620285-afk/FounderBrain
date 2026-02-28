package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HealthResponse {
    private int healthScore;
    private double growthRate;
    private double runwayMonths;
    private String riskLevel;
    private String recommendation;
}
