package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardResponse {
    private String founderEmail;
    private String startupName;
    private String industry;
    private Double revenue;
    private int totalStartups;

}
