package com.mrs.enpoint.feature.auth.service;

import com.mrs.enpoint.feature.auth.dto.AuthResponseDTO;
import com.mrs.enpoint.feature.auth.dto.ChangePasswordRequestDTO;
import com.mrs.enpoint.feature.auth.dto.PasswordResetUpdateDTO;
import com.mrs.enpoint.feature.auth.dto.RegisterRequestDTO;
import com.mrs.enpoint.feature.auth.dto.UpdateProfileRequestDTO;
import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    void register(RegisterRequestDTO request);

    AuthResponseDTO login(String email, String password);
    
    AuthResponseDTO refreshAccessToken(String refreshToken);

    void changePassword(ChangePasswordRequestDTO request);

    UserResponseDTO updateProfile(UpdateProfileRequestDTO request);
    
    UserResponseDTO getProfileDetails();
    
    void logout(HttpServletRequest request);
    
    public void sendOtpForPasswordReset(String email);
    
    public void resetPasswordWithOtp(PasswordResetUpdateDTO request);
}
