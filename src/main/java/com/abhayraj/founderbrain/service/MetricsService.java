package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.dto.BenchmarkResponse;
import com.abhayraj.founderbrain.dto.HealthResponse;
import com.abhayraj.founderbrain.dto.MetricsResponse;
import com.abhayraj.founderbrain.exception.StartupNotFoundException;
import com.abhayraj.founderbrain.model.IndustryBenchmark;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.repository.IndustryBenchmarkRepository;
import com.abhayraj.founderbrain.repository.StartupRepository;
import com.abhayraj.founderbrain.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final UserActivityRepository activityRepository;
    private final StartupRepository startupRepository;
    private final HealthCalculationService healthCalculationService;
    private final IndustryBenchmarkRepository industryBenchmarkRepository;

    public MetricsResponse getStartupMetrics(Long startupId) {

        long totalActivities =
                activityRepository.countByStartupId(startupId);

        long activeUsers =
                activityRepository.countActiveUsersByStartupId(startupId);

        Double avgSession =
                activityRepository.getAverageSessionDuration(startupId);

        List<Object[]> featureStats =
                activityRepository.findFeatureUsageStats(startupId);

        return new MetricsResponse(
                totalActivities,
                activeUsers,
                avgSession,
                featureStats
        );
    }
    public HealthResponse analyzeStartup(Long startupId) {

        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new StartupNotFoundException("Startup not found"));

        double growthRate = healthCalculationService
                .calculateGrowthRate(startup.getRevenue(), startup.getLastMonthRevenue());

        double runway = healthCalculationService
                .calculateRunway(startup.getCashReserve(), startup.getMonthlyExpenses());

        int healthScore = healthCalculationService
                .calculateHealthScore(growthRate, runway);

        String riskLevel = healthCalculationService
                .determineRisk(healthScore);

        return new HealthResponse(
                healthScore,
                growthRate,
                runway,
                riskLevel,
                generateRecommendation(riskLevel)
        );
    }
    private String generateRecommendation(String riskLevel) {
        return switch (riskLevel) {
            case "LOW" -> "Strong performance. Focus on scaling.";
            case "MEDIUM" -> "Monitor burn rate and improve growth.";
            case "HIGH" -> "Urgent attention needed. Reduce expenses and increase revenue.";
            default -> "No recommendation available.";
        };
    }
    public BenchmarkResponse compareWithIndustry(Long startupId){
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new RuntimeException("Startup not found"));
        double growth = healthCalculationService.calculateGrowthRate(
                startup.getRevenue(),
                startup.getLastMonthRevenue()
        );
        double runway = healthCalculationService.calculateRunway(
                startup.getCashReserve(),
                startup.getMonthlyExpenses()
        );
        IndustryBenchmark benchmark = industryBenchmarkRepository
                .findByIndustry(startup.getIndustry())
                .orElseThrow(() -> new RuntimeException("No benchmark found"));

        String performance;
        if(growth> benchmark.getAverageGrowthRate() && runway> benchmark.getAverageRunwayMonths()){
            performance = "Above Industry Average";
        }
        else if(growth> benchmark.getAverageGrowthRate()){
            performance =  "Strong Growth but Weak Runway";
        }
        else if(runway > benchmark.getAverageRunwayMonths()){
            performance = "Stable but Slow Growth";
        }
        else {
            performance = "Below Industry Average";
        }
        return new BenchmarkResponse(
                growth,
                benchmark.getAverageGrowthRate(),
                runway,
                benchmark.getAverageRunwayMonths(),
                performance
        );

    }
}
