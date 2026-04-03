package com.mrs.enpoint.feature.auth.mapper;

import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auth.dto.AuthResponseDTO;

public class AuthMapper {

    public static AuthResponseDTO toAuthResponseDTO(User user, String accessToken, String refreshToken) {
        return new AuthResponseDTO(
            accessToken,
            refreshToken,
            user.getEmail(),
            user.getFullName(),
            user.getRole().getRoleName().name()
        );
    }
}