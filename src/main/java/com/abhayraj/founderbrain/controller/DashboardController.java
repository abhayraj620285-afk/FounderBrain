package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.dto.ApiResponse;
import com.abhayraj.founderbrain.dto.DashboardResponse;
import com.abhayraj.founderbrain.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('FOUNDER')")
    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard() {

        DashboardResponse response =  dashboardService.getDashboard();
        return new  ApiResponse<>(
                "success",
                "Get Dashboard successfully",
                response
        );
    }
}
