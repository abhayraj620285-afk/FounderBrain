package com.abhayraj.founderbrain.service;

import com.abhayraj.founderbrain.exception.UserNotFoundException;
import com.abhayraj.founderbrain.model.Startup;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.repository.StartupRepository;
import com.abhayraj.founderbrain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StartupRepository startupRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void changeUserRole(Long id,String role){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        user.setRole(role);
        userRepository.save(user);

        }
    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }
    }

