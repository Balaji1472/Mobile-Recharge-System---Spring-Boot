package com.mrs.enpoint.feature.user.service;

import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;
import com.mrs.enpoint.feature.auth.mapper.UserMapper;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.user.dto.UserStatusRequestDTO;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public UserServiceImpl(UserRepository userRepository, AuditService auditService, SecurityUtils securityUtils) {
		this.userRepository = userRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserResponseDTO> getAllUsers() {
		return userRepository.findAll().stream().map(user -> UserMapper.toResponseDTO(user)).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponseDTO getUserById(int id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found with id: " + id));
		return UserMapper.toResponseDTO(user);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public UserResponseDTO updateUserStatus(int id, UserStatusRequestDTO request) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found with id: " + id));

		String oldStatus = user.getStatus().name();
		user.setStatus(request.getStatus());
		User saved = userRepository.save(user);

		AuditAction action = switch (request.getStatus()) {
		case BLOCKED -> AuditAction.USER_BLOCK;
		case ACTIVE -> AuditAction.USER_UNBLOCK;
		case INACTIVE -> AuditAction.USER_INACTIVE;
		};

		auditService.log(securityUtils.getCurrentUserId(), EntityName.USER, saved.getUserId(), action, oldStatus,
				saved.getStatus().name());

		return UserMapper.toResponseDTO(saved);
	}
}