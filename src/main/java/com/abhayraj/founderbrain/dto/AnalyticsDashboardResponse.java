package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsDashboardResponse {
    private int healthScore;
    private double growthRate;
    private double runwayMonths;
    private String riskLevel;

    private String growthInsight;
    private String runwayInsight;
    private String riskInsight;
    private String recommendation;

    private String fundingSuggestion;
    private double industryAvgGrowth;
    private double industryAvgRunway;
}
