package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.dto.*;
import com.abhayraj.founderbrain.exception.StartupNotFoundException;
import com.abhayraj.founderbrain.model.IndustryBenchmark;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.repository.IndustryBenchmarkRepository;
import com.abhayraj.founderbrain.repository.StartupRepository;
import com.abhayraj.founderbrain.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsService {
    private final UserActivityRepository activityRepository;
    private final StartupRepository startupRepository;
    private final HealthCalculationService healthCalculationService;
    private final IndustryBenchmarkRepository industryBenchmarkRepository;
    private final MlService mlService;

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
    public FundingResponse analyzeFunding(Long startupId){
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
        String signal;
        String reason;
        int confidence = 60;
        if(growth>20 && runway<6){
            signal = "RAISE_CAPITAL";
            reason = "Strong growth but runway is low. Consider raising funds.";
            confidence = 85;
        }
        else if (growth < 5 && runway > 12) {
            signal = "OPTIMIZE";
            reason = "Low growth despite strong runway. Improve product or marketing.";
            confidence = 75;
        }
        else if (growth > 15 && runway > 9) {
            signal = "SCALE";
            reason = "Healthy growth and solid runway. Focus on scaling.";
            confidence = 80;
        }
        else {
            signal = "STABLE";
            reason = "Balanced metrics. Monitor performance closely.";
            confidence = 65;
        }

        return new FundingResponse(signal, reason, confidence);
    }
    public StartupInsightResponse generateInsights(Long startupId){
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
        int score = healthCalculationService.calculateHealthScore(growth, runway);

        String risk = healthCalculationService.determineRisk(score);

        String growthInsight;
        String runwayInsight;
        String recommendation;
        // growth insight
        if (growth > 20) {
            growthInsight = "Your startup is experiencing strong growth.";
        } else if (growth > 10) {
            growthInsight = "Your startup growth is healthy.";
        } else {
            growthInsight = "Growth is slow. Consider improving product or marketing.";
        }
        // Runway insight
        if (runway > 12) {
            runwayInsight = "Your startup has a strong financial runway.";
        } else if (runway >= 6) {
            runwayInsight = "Runway is moderate. Monitor expenses carefully.";
        } else {
            runwayInsight = "Runway is low. Consider raising capital soon.";
        }
        // Recommendation
        if (growth > 20 && runway < 6) {
            recommendation = "Strong growth but low runway. Prepare for fundraising.";
        } else if (growth < 5 && runway > 12) {
            recommendation = "Low growth despite strong runway. Focus on product improvement.";
        } else if (growth > 15 && runway > 9) {
            recommendation = "Healthy startup. Focus on scaling operations.";
        } else {
            recommendation = "Maintain balanced growth and monitor financial health.";
        }

        String riskInsight = "Your startup currently has " + risk + " operational risk.";

        return new StartupInsightResponse(
                growthInsight,
                runwayInsight,
                riskInsight,
                recommendation
        );

    }
    public AnalyticsDashboardResponse getDashboard(Long startupId){
        log.info("[startupId={}] Fetching dashboard", startupId);
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(()-> new RuntimeException("Exception not found"));
        double growth = healthCalculationService.calculateGrowthRate(
                startup.getRevenue(),
                startup.getLastMonthRevenue()
        );
        log.info("Growth calculated: {}", growth);
        double runway = healthCalculationService.calculateRunway(
                startup.getCashReserve(),
                startup.getMonthlyExpenses()
        );
        log.info("[startupId={}] Runway calculated: {}", startupId, runway);
        int score = healthCalculationService.calculateHealthScore(growth,runway);
        log.info("[startupId={}] Health score: {}", startupId, score);
        String risk = mlService.getRiskPrediction(startup);
        log.info("[startupId={}] Risk level: {}", startupId, risk);
        // Insights reuse
        StartupInsightResponse insights = generateInsights(startupId);

        // Dummy funding logic (upgrade later)
        String fundingSuggestion;
        if (score > 75) {
            fundingSuggestion = "You are ready to raise funding.";
        } else if (score > 50) {
            fundingSuggestion = "Improve metrics before fundraising.";
        } else {
            fundingSuggestion = "Focus on survival before raising funds.";
        }

        // Dummy benchmark (replace later with DB)
        // Benchmark from DB
        IndustryBenchmark benchmark = industryBenchmarkRepository
                .findByIndustry(startup.getIndustry())
                .orElseThrow(() -> new RuntimeException("Benchmark not found"));

        double avgGrowth = benchmark.getAverageGrowthRate();
        double avgRunway = benchmark.getAverageRunwayMonths();
        log.info("Benchmark - Avg Growth: {}, Avg Runway: {}", avgGrowth, avgRunway);
        return new AnalyticsDashboardResponse(
                score,
                growth,
                runway,
                risk,
                insights.getGrowthInsight(),
                insights.getRunwayInsight(),
                insights.getRiskInsight(),
                insights.getActionRecommendation(),
                fundingSuggestion,
                avgGrowth,
                avgRunway
        );
     }
}
