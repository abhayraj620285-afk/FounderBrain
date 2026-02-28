package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StartupResponse {
    private Long id;
    private String name;
    private String industry;
    private Double revenue;
    private Integer users;
    private Double lastMonthRevenue;
    private Double monthlyExpenses;
    private Double cashReserve;

}
