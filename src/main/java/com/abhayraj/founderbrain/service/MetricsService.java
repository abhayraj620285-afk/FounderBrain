package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.dto.MetricsResponse;
import com.abhayraj.founderbrain.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final UserActivityRepository activityRepository;

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
}
