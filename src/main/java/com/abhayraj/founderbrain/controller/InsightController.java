package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.dto.InsightResponse;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/insights")
@RequiredArgsConstructor
public class InsightController {
    private final InsightService insightService;

    @GetMapping("/{startupId}")
    public InsightResponse getInsights(
            @PathVariable Long startupId) {

        return insightService.generateInsights(startupId);
    }
    @GetMapping("/my")
    public InsightResponse getMyInsight(){
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long startupId = user.getStartupId();

        return insightService.generateInsights(startupId);
    }
}
