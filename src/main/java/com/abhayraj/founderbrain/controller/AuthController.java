package com.abhayraj.founderbrain.controller;

import com.abhayraj.founderbrain.Security.JwtAuthenticationFilter;
import com.abhayraj.founderbrain.Security.JwtService;
import com.abhayraj.founderbrain.dto.AuthRequest;
import com.abhayraj.founderbrain.dto.AuthResponse;
import com.abhayraj.founderbrain.model.RefreshToken;
import com.abhayraj.founderbrain.model.User;
import com.abhayraj.founderbrain.repository.RefreshTokenRepository;
import com.abhayraj.founderbrain.repository.UserRepository;
import com.abhayraj.founderbrain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request){
       User user = new User();
       user.setEmail(request.getEmail());
       user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return "User Registered Successfully";
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request){
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid credentials");
        }
        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken()
        );

    }
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestParam String  refreshToken){
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(token);

        String newAccessToken =
                jwtService.generateToken(token.getUser().getEmail());

        return new AuthResponse(
                newAccessToken,
                refreshToken
        );

    }
}
