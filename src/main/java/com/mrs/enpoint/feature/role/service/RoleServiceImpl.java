package com.mrs.enpoint.feature.role.service;

import com.mrs.enpoint.entity.Role;
import com.mrs.enpoint.feature.auth.repository.RoleRepository;
import com.mrs.enpoint.feature.role.dto.RoleResponseDTO;
import com.mrs.enpoint.feature.role.mapper.RoleMapper;
import com.mrs.enpoint.shared.exception.NotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<RoleResponseDTO> getAllRoles() {

		List<Role> roles = roleRepository.findAll();

		if (roles.isEmpty()) {
			throw new NotFoundException("No roles found in the system");
		}

		return roles.stream().map(role -> RoleMapper.toResponseDTO(role)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public RoleResponseDTO getRoleById(int roleId) {

		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));

		return RoleMapper.toResponseDTO(role);
	}
}