package com.abhayraj.founderbrain.repository;

import com.abhayraj.founderbrain.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long>{

    long countByStartupId(Long startupId);

    @Query("SELECT COUNT(DISTINCT ua.user.id) FROM UserActivity ua WHERE ua.startup.id = :startupId")
    long countActiveUsersByStartupId(Long startupId);

    @Query("""
    SELECT ua.featureName, COUNT(ua)
    FROM UserActivity ua
    WHERE ua.startup.id = :startupId
    GROUP BY ua.featureName
    ORDER BY COUNT(ua) DESC
""")
    List<Object[]> findMostUsedFeature(Long startupId);

    @Query("SELECT AVG(ua.sessionDuration) FROM UserActivity ua WHERE ua.startup.id = :startupId")
    Double getAverageSessionDuration(Long startupId);
    @Query("""
    SELECT MAX(ua.timestamp)
    FROM UserActivity ua
    WHERE ua.user.id = :userId
    AND ua.startup.id = :startupId
""")
    LocalDateTime findLastActivity(Long userId, Long startupId);
    @Query("""
    SELECT COUNT(ua)
    FROM UserActivity ua
    WHERE ua.user.id = :userId
    AND ua.startup.id = :startupId
    AND ua.timestamp >= :since
""")
    long countRecentActivities(Long userId, Long startupId, LocalDateTime since);
    @Query("""
    SELECT DISTINCT ua.user.id
    FROM UserActivity ua
    WHERE ua.startup.id = :startupId
""")
    List<Long> findUserIdsByStartup(Long startupId);
    @Query("""
    SELECT ua.featureName, COUNT(ua)
    FROM UserActivity ua
    WHERE ua.startup.id = :startupId
    GROUP BY ua.featureName
    ORDER BY COUNT(ua) DESC
""")
    List<Object[]> findFeatureUsageStats(@Param("startupId") Long startupId);

}
