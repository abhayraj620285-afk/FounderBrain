package com.abhayraj.founderbrain.controller;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

}
