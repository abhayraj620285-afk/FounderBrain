package com.abhayraj.founderbrain.controller;
import com.abhayraj.founderbrain.dto.ApiResponse;
import com.abhayraj.founderbrain.dto.StartupRequest;
import com.abhayraj.founderbrain.dto.StartupResponse;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.service.StartupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/startups")
@RequiredArgsConstructor
public class StartupController {
    private final StartupService startupService;

    @PreAuthorize("hasRole('FOUNDER')")
    @PostMapping
    public ApiResponse<StartupResponse> create(@Valid @RequestBody StartupRequest request) {
        StartupResponse response =  startupService.createStartup(request);
        return new ApiResponse<>(
                "success",
                "Startup created successfully",
                response
        );

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/my")
    public ApiResponse<List<StartupResponse>> myStartups() {

        List<StartupResponse>  response =  startupService.getMyStartups();
        return new ApiResponse<>(
                "success",
                "Fetched all Startup successfully",
                response
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<StartupResponse> getOne(@PathVariable Long id) {
        StartupResponse response = startupService.getStartupById(id);
        return new ApiResponse<>(
                "success",
                "Startup fetched successfully",
                response
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<StartupResponse> update(@PathVariable Long id,
                          @Valid @RequestBody StartupRequest request) {
        StartupResponse response = startupService.updateStartup(id, request);
        return new ApiResponse<>(
                "success",
                "Startup updated successfully",
                response
        );

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        startupService.deleteStartup(id);
    }
}
