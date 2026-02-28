package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.exception.UserNotFoundException;
import com.abhayraj.founderbrain.model.*;
import com.abhayraj.founderbrain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final UserActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final StartupRepository startupRepository;

    public UserActivity logActivity(Long userId, Long startupId, UserActivity activity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        activity.setUser(user);
        activity.setStartup(startup);
        activity.setTimestamp(LocalDateTime.now());

        return activityRepository.save(activity);
    }
}
