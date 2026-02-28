package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.model.UserActivity;
import com.abhayraj.founderbrain.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/{userId}/{startupId}")
    public UserActivity logActivity(
            @PathVariable Long userId,
            @PathVariable Long startupId,
            @RequestBody UserActivity activity
    ) {
        return activityService.logActivity(userId, startupId, activity);
    }
}

