package com.abhayraj.founderbrain.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MetricsResponse {

    private long totalActivities;
    private long activeUsers;
    private Double averageSessionDuration;
    private List<Object[]> featureStats;
}
