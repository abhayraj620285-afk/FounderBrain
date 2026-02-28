package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService metricsService;

    @GetMapping("/{startupId}")
    public Map<String, Object> getMetrics(@PathVariable Long startupId) {
        return (Map<String, Object>) metricsService.getStartupMetrics(startupId);
    }
}
