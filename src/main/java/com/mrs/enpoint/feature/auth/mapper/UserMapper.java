package com.mrs.enpoint.feature.auth.mapper;

import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;

public class UserMapper {

	private UserMapper() {
		
	}
	
    public static UserResponseDTO toResponseDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setRole(user.getRole().getRoleName());
        dto.setStatus(user.getStatus().name());

        return dto;
    }
}
