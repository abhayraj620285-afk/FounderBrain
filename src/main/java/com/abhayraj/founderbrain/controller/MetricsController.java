package com.abhayraj.founderbrain.controller;

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
}
