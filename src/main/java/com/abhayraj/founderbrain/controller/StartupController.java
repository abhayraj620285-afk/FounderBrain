package com.abhayraj.founderbrain.controller;
import com.abhayraj.founderbrain.dto.StartupRequest;
import com.abhayraj.founderbrain.dto.StartupResponse;
import com.abhayraj.founderbrain.service.StartupService;
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
    public StartupResponse create(@RequestBody StartupRequest request) {
        return startupService.createStartup(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/my")
    public List<StartupResponse> myStartups() {
        return startupService.getMyStartups();
    }

    @GetMapping("/{id}")
    public StartupResponse getOne(@PathVariable Long id) {
        return startupService.getStartupById(id);
    }

    @PutMapping("/{id}")
    public StartupResponse update(@PathVariable Long id,
                          @RequestBody StartupRequest request) {
        return startupService.updateStartup(id, request);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        startupService.deleteStartup(id);
    }
}
