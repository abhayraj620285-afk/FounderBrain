package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.dto.*;
import com.abhayraj.founderbrain.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService metricsService;

    @GetMapping("/health/{startupId}")
    public ApiResponse<HealthResponse> getHealth(@PathVariable Long startupId) {
        HealthResponse response =  metricsService.analyzeStartup(startupId);
        return new ApiResponse<>(
                "success",
                "Health analysis fetched",
                response
        );
    }
    @PreAuthorize("hasAnyRole('FOUNDER','ADMIN')")
    @GetMapping("/benchmark/{startupId}")
    public ApiResponse<BenchmarkResponse> compare(@PathVariable Long startupId) {

        BenchmarkResponse response = metricsService.compareWithIndustry(startupId);

        return new ApiResponse<>(
                "success",
                "Benchmark comparison fetched",
                response
        );
    }
    @GetMapping("/funding/{startupId}")
    public ApiResponse<FundingResponse> analyzeFunding(@PathVariable Long startupId) {

        FundingResponse response =  metricsService.analyzeFunding(startupId);
        return new ApiResponse<>(
                "success",
                "Funding analysis fetched",
                response
        );
    }
    @PreAuthorize("hasAnyRole('FOUNDER','ADMIN')")
    @GetMapping("/insights/{startupId}")
    public ApiResponse<StartupInsightResponse> getInsights(@PathVariable Long startupId) {

        StartupInsightResponse response = metricsService.generateInsights(startupId);

        return new ApiResponse<>(
                "success",
                "Startup insights fetched",
                response
        );
    }
    @PreAuthorize("hasAnyRole('FOUNDER','ADMIN')")
    @GetMapping("/dashboard/{startupId}")
    public ApiResponse<AnalyticsDashboardResponse> getDashboard(@PathVariable Long startupId) {

        AnalyticsDashboardResponse response = metricsService.getDashboard(startupId);

        return new ApiResponse<>(
                "success",
                "Dashboard fetched successfully",
                response
        );
    }

}
