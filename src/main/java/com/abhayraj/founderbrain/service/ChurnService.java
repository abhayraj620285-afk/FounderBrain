package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChurnService {
    private final UserActivityRepository activityRepository;

    public Map<String, Object> calculateChurn(Long startupId) {

        List<Long> userIds =
                activityRepository.findUserIdsByStartup(startupId);

        int highRisk = 0;
        int mediumRisk = 0;
        int lowRisk = 0;

        LocalDateTime now = LocalDateTime.now();

        for (Long userId : userIds) {

            LocalDateTime lastActivity =
                    activityRepository.findLastActivity(userId, startupId);

            if (lastActivity == null ||
                    lastActivity.isBefore(now.minusDays(14))) {

                highRisk++;

            } else {

                long recentCount =
                        activityRepository.countRecentActivities(
                                userId,
                                startupId,
                                now.minusDays(7)
                        );

                if (recentCount < 3) {
                    mediumRisk++;
                } else {
                    lowRisk++;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("highRiskUsers", highRisk);
        result.put("mediumRiskUsers", mediumRisk);
        result.put("lowRiskUsers", lowRisk);

        return result;
    }
}
