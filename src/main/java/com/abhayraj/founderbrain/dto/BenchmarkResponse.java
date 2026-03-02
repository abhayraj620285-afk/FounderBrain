package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BenchmarkResponse {
    private double yourGrowth;
    private double industryAverageGrowth;

    private double yourRunway;
    private double industryAverageRunway;

    private String performance;
}
