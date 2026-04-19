package com.abhayraj.founderbrain.service;
import com.abhayraj.founderbrain.dto.StartupRequest;
import com.abhayraj.founderbrain.dto.StartupResponse;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.repository.StartupRepository;
import com.abhayraj.founderbrain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
@Slf4j
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
        log.error("User not authenticated properly");
        throw new RuntimeException("User not authenticated properly");
    }
    public StartupResponse createStartup(StartupRequest request) {
        try{
            User user = getLoggedUser();
            log.info("[userId={}] Creating startup with name={}", user.getId(), request.getName());
            Startup startup = new Startup();
            startup.setIndustry(request.getIndustry());
            startup.setName(request.getName());
            startup.setRevenue(request.getRevenue());
            startup.setUsers(request.getUsers());
            startup.setUser(getLoggedUser());
            startup.setLastMonthRevenue(request.getLastMonthRevenue());
            startup.setMonthlyExpenses(request.getMonthlyExpenses());
            startup.setCashReserve(request.getCashReserve());
            Startup savedStartup = startupRepository.save(startup);
            log.info("[userId={}] Created startup with name={}", user.getId(), request.getName());
            return new StartupResponse(
                    savedStartup.getId(),
                    savedStartup.getName(),
                    savedStartup.getIndustry(),
                    savedStartup.getRevenue(),
                    savedStartup.getUsers(),
                    savedStartup.getLastMonthRevenue(),
                    startup.getMonthlyExpenses(),
                    startup.getCashReserve()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<StartupResponse> getMyStartups() {
        User user = getLoggedUser();
        log.info("[userId={}] Fetching user startups", user.getId());
       List<Startup> startups = startupRepository.findByUserId(user.getId());
        return startups.stream()
                .map(startup -> new StartupResponse(
                        startup.getId(),
                        startup.getName(),
                        startup.getIndustry(),
                        startup.getRevenue(),
                        startup.getUsers(),
                        startup.getCashReserve(),
                        startup.getMonthlyExpenses(),
                        startup.getLastMonthRevenue()
                ))
                .toList();
    }
    public StartupResponse getStartupById(Long id){
        User loggedUser = getLoggedUser();
        log.info("[userId={}] Fetching startup id={}", loggedUser.getId(), id);
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Startup not found with id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Startup not found");
                });

        if (!startup.getUser().getId().equals(loggedUser.getId())) {
            log.warn("[userId={}] Unauthorized access to startup id={}", loggedUser.getId(), id);
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
                startup.getUsers(),
                startup.getLastMonthRevenue(),
                startup.getMonthlyExpenses(),
                startup.getCashReserve()

        );

    }
    public StartupResponse updateStartup(Long id,StartupRequest request){
        User user = getLoggedUser();
        log.info("[userId={}] Updating startup id={}", user.getId(), id);
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // OwnerShip validation
        if (!startup.getUser().getId().equals(user.getId())) {
            log.warn("[userId={}] Unauthorized update attempt for startup id={}", user.getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update this startup"
            );
        }
        startup.setName(request.getName());
        startup.setIndustry(request.getIndustry());
        startup.setRevenue(request.getRevenue());
        startup.setUsers(request.getUsers());
        startup.setLastMonthRevenue(request.getLastMonthRevenue());
        startup.setMonthlyExpenses(request.getMonthlyExpenses());
        startup.setCashReserve(request.getCashReserve());
        Startup updated = startupRepository.save(startup);
        log.info("[startupId={}] Startup updated successfully", id);

        return new StartupResponse(
                updated.getId(),
                updated.getName(),
                updated.getIndustry(),
                updated.getRevenue(),
                updated.getUsers(),
                updated.getLastMonthRevenue(),
                updated.getMonthlyExpenses(),
                updated.getCashReserve()


        );

    }
    public void deleteStartup(Long id){
    User user = getLoggedUser();
        log.info("[userId={}] Deleting startup id={}", user.getId(), id);
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!startup.getUser().getId().equals(user.getId())) {
            log.warn("[userId={}] Unauthorized delete attempt for startup id={}", user.getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to delete this startup"
            );

        }

        startupRepository.delete(startup);
        log.info("[startupId={}] Startup deleted successfully", id);
    }
    public List<StartupResponse> getAllStartups() {
        log.info("Fetching all startups (admin)");
        List<Startup> startups = startupRepository.findAll();

        return startups.stream()
                .map(s -> new StartupResponse(
                        s.getId(),
                        s.getName(),
                        s.getIndustry(),
                        s.getRevenue(),
                        s.getUsers(),
                        s.getMonthlyExpenses(),
                        s.getCashReserve(),
                        s.getLastMonthRevenue()
                ))
                .toList();
    }


}
