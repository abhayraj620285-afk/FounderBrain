package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartupInsightResponse {
    private String growthInsight;
    private String runwayInsight;
    private String riskInsight;
    private String actionRecommendation;

}
