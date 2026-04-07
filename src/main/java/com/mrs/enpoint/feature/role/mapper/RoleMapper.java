package com.mrs.enpoint.feature.role.mapper;

import com.mrs.enpoint.entity.Role;
import com.mrs.enpoint.feature.role.dto.RoleResponseDTO;

public class RoleMapper {

	private RoleMapper() {
	}

	public static RoleResponseDTO toResponseDTO(Role role) {
		RoleResponseDTO dto = new RoleResponseDTO();
		dto.setRoleId(role.getRoleId());
		dto.setRoleName(role.getRoleName());
		dto.setDescription(role.getDescription());
		dto.setCreatedAt(role.getCreatedAt());
		return dto;
	}
}