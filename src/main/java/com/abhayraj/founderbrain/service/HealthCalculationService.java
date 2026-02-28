package com.abhayraj.founderbrain.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCalculationService {
    public double calculateGrowthRate(Double revenue,Double lastMonthRevenue){
        if(revenue==null || lastMonthRevenue==null || lastMonthRevenue==0){
            return 0;
        }
        return ((revenue-lastMonthRevenue)/lastMonthRevenue)*100;
    }
    public double calculateRunway(Double cashReserve,Double monthlyExpenses){
        if(monthlyExpenses==null || cashReserve==null) return 0;
        return cashReserve/monthlyExpenses;
    }
    public int calculateHealthScore(double growthRate,double runwayMonths){
        int score = 20;
        if(growthRate>20) score+=20;
        else if(growthRate>10) score+=10;
        else if(growthRate<0) score-=20;

        if(runwayMonths>12) score+=20;
        else if(runwayMonths<6) score-=20;
        return Math.max(0, Math.min(score, 100));
    }
    public String determineRisk(int healthScore) {
        if (healthScore >= 75) return "LOW";
        if (healthScore >= 50) return "MEDIUM";
        return "HIGH";
    }
}

