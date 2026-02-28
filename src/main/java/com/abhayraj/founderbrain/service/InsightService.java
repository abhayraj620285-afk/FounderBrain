package com.abhayraj.founderbrain.service;


import com.abhayraj.founderbrain.dto.InsightResponse;
import com.abhayraj.founderbrain.dto.MetricsResponse;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InsightService {
    private final ChurnService churnService;
    private final MetricsService metricsService;
    private final StartupRepository startupRepository;

    public InsightResponse generateInsights(Long startupId) {

        Map<String, Object> churnData =
                churnService.calculateChurn(startupId);

        MetricsResponse metrics =
                metricsService.getStartupMetrics(startupId);

        int highRiskUsers = (int) churnData.get("highRiskUsers");
        int mediumRiskUsers = (int) churnData.get("mediumRiskUsers");
        int lowRiskUsers = (int) churnData.get("lowRiskUsers");

        int totalUsers = highRiskUsers + mediumRiskUsers + lowRiskUsers;

        double churnPercentage = 0;
        List<String> suggestions = new ArrayList<>();

        if (totalUsers > 0) {
            churnPercentage =
                    ((double) highRiskUsers / totalUsers) * 100;
        }

        if (churnPercentage > 30) {
            suggestions.add("Retention campaign needed");
        }

        Double averageSession = metrics.getAverageSessionDuration();

        if (averageSession != null && averageSession < 60) {
            suggestions.add("Users not engaging deeply");
        }

        long totalActivities = metrics.getTotalActivities();
        List<Object[]> featureStats = metrics.getFeatureStats();

        if (featureStats != null &&
                !featureStats.isEmpty() &&
                totalActivities > 0) {

            Object[] topFeature = featureStats.get(0);
            Long topFeatureCount = (Long) topFeature[1];

            double dominancePercentage =
                    ((double) topFeatureCount / totalActivities) * 100;

            if (dominancePercentage > 80) {
                suggestions.add("Other features underperforming");
            }
        }
        User loggedUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new RuntimeException("Startup not found"));
        if (!startup.getUser().getId().equals(loggedUser.getId())) {
            throw new RuntimeException("You are not allowed to access this startup");
        }
        return new InsightResponse(
                churnPercentage,
                suggestions
        );
    }
}
