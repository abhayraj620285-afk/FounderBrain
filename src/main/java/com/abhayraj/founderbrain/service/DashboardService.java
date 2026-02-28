package com.abhayraj.founderbrain.service;

import com.abhayraj.founderbrain.dto.DashboardResponse;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.repository.StartupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final StartupRepository startupRepository;
    public DashboardResponse getDashboard(){
        User loggedUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        List<Startup> startups = startupRepository.findByUserId(loggedUser.getId());
        if (startups.isEmpty()) {
            return new DashboardResponse(
                    loggedUser.getEmail(),
                    "No Startup",
                    "N/A",
                    0D,
                    0
            );
        }
        Startup firstStartup = startups.get(0);

        return new DashboardResponse(
                loggedUser.getEmail(),
                firstStartup.getName(),
                firstStartup.getIndustry(),
                firstStartup.getRevenue(),
                startups.size()
        );

    }
}
