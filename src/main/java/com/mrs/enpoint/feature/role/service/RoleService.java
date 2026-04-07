package com.mrs.enpoint.feature.role.service;

import com.mrs.enpoint.feature.role.dto.RoleResponseDTO;

import java.util.List;

public interface RoleService {

    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(int roleId);
}