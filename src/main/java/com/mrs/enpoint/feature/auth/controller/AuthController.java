package com.mrs.enpoint.feature.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrs.enpoint.feature.auth.dto.AuthResponseDTO;
import com.mrs.enpoint.feature.auth.dto.ChangePasswordRequestDTO;
import com.mrs.enpoint.feature.auth.dto.LoginRequestDTO;
import com.mrs.enpoint.feature.auth.dto.RefreshTokenRequestDTO;
import com.mrs.enpoint.feature.auth.dto.RegisterRequestDTO;
import com.mrs.enpoint.feature.auth.dto.UpdateProfileRequestDTO;
import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;
import com.mrs.enpoint.feature.auth.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request.getRefreshToken()));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        
    	authService.logout(request);
        return ResponseEntity.ok("Logged out successfully. Token revoked");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        authService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(@Valid @RequestBody UpdateProfileRequestDTO request) {
        return ResponseEntity.ok(authService.updateProfile(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(){
    	return ResponseEntity.ok(authService.getProfileDetails());
    }
}
