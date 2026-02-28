package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.dto.StartupRequest;
import com.abhayraj.founderbrain.dto.StartupResponse;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.repository.StartupRepository;
import com.abhayraj.founderbrain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final StartupRepository startupRepository;
    private final UserRepository userRepository;
    private User getLoggedUser() {

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        throw new RuntimeException("User not authenticated properly");
    }
    public StartupResponse createStartup(StartupRequest request) {
        User user = getLoggedUser();
        Startup startup = new Startup();
        startup.setIndustry(request.getIndustry());
        startup.setName(request.getName());
        startup.setRevenue(request.getRevenue());
        startup.setUsers(request.getUsers());
        startup.setUser(getLoggedUser());
        Startup savedStartup = startupRepository.save(startup);
        return new StartupResponse(
                savedStartup.getId(),
                savedStartup.getName(),
                savedStartup.getIndustry(),
                savedStartup.getRevenue(),
                savedStartup.getUsers()
        );
    }
    public List<StartupResponse> getMyStartups() {
        User user = getLoggedUser();
       List<Startup> startups = startupRepository.findByUserId(user.getId());
        return startups.stream()
                .map(startup -> new StartupResponse(
                        startup.getId(),
                        startup.getName(),
                        startup.getIndustry(),
                        startup.getRevenue(),
                        startup.getUsers()
                ))
                .toList();
    }
    public StartupResponse getStartupById(Long id){
        User loggedUser = getLoggedUser();
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Startup not found"
                        ));
        if (!startup.getUser().getId().equals(loggedUser.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to access this startup"
            );
        }
        return new StartupResponse(
                startup.getId(),
                startup.getName(),
                startup.getIndustry(),
                startup.getRevenue(),
                startup.getUsers()
        );

    }
    public StartupResponse updateStartup(Long id,StartupRequest request){
        User user = getLoggedUser();
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // OwnerShip validation
        if (!startup.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update this startup"
            );
        }
        startup.setName(request.getName());
        startup.setIndustry(request.getIndustry());
        startup.setRevenue(request.getRevenue());
        startup.setUsers(request.getUsers());

        Startup updated = startupRepository.save(startup);

        return new StartupResponse(
                updated.getId(),
                updated.getName(),
                updated.getIndustry(),
                updated.getRevenue(),
                updated.getUsers()
        );

    }
    public void deleteStartup(long id){
    User user = getLoggedUser();
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!startup.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to delete this startup"
            );
        }

        startupRepository.delete(startup);
    }
    public List<StartupResponse> getAllStartups() {

        List<Startup> startups = startupRepository.findAll();

        return startups.stream()
                .map(s -> new StartupResponse(
                        s.getId(),
                        s.getName(),
                        s.getIndustry(),
                        s.getRevenue(),
                        s.getUsers()
                ))
                .toList();
    }


}
