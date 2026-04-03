package com.mrs.enpoint.feature.user.service;

import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;
import com.mrs.enpoint.feature.user.dto.UserStatusRequestDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(int id);

    UserResponseDTO updateUserStatus(int id, UserStatusRequestDTO request);
}