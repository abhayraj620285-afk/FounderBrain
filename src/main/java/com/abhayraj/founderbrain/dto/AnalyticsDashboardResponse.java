package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.databind.node.DoubleNode;

@Data
@AllArgsConstructor
public class AnalyticsDashboardResponse {
    private int healthScore;
    private double growthRate;
    private double runwayMonths;
    private String riskLevel;
    private Double riskConfidence;

    private String growthInsight;
    private String runwayInsight;
    private String riskInsight;
    private String recommendation;

    private String fundingSuggestion;
    private double industryAvgGrowth;
    private double industryAvgRunway;
}
