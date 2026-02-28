package com.abhayraj.founderbrain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartupRequest {
    private String name;
    private String industry;
    private Double revenue;
    private Integer users;
    private Double lastMonthRevenue;
    private Double monthlyExpenses;
    private Double cashReserve;

}
