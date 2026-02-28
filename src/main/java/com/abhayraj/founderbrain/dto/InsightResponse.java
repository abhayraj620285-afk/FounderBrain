package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InsightResponse {
    private double churnPercentage;
    private List<String> suggestions;
}
