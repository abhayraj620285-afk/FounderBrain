package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.dto.BenchmarkResponse;
import com.abhayraj.founderbrain.dto.FundingResponse;
import com.abhayraj.founderbrain.dto.HealthResponse;
import com.abhayraj.founderbrain.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService metricsService;

    @GetMapping("/health/{startupId}")
    public HealthResponse getHealth(@PathVariable Long startupId) {
        return metricsService.analyzeStartup(startupId);
    }
    @GetMapping("/benchmark/{startupId}")
    public BenchmarkResponse compare(@PathVariable Long startupId) {
        return metricsService.compareWithIndustry(startupId);
    }
    @GetMapping("/funding/{startupId}")
    public FundingResponse analyzeFunding(@PathVariable Long startupId) {
        return metricsService.analyzeFunding(startupId);
    }
}
