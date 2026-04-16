package com.mrs.enpoint.feature.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrs.enpoint.entity.RevokedToken;
import com.mrs.enpoint.entity.Role;
import com.mrs.enpoint.entity.User;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.auth.dto.AuthResponseDTO;
import com.mrs.enpoint.feature.auth.dto.ChangePasswordRequestDTO;
import com.mrs.enpoint.feature.auth.dto.RegisterRequestDTO;
import com.mrs.enpoint.feature.auth.dto.UpdateProfileRequestDTO;
import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;
import com.mrs.enpoint.feature.auth.enums.RoleType;
import com.mrs.enpoint.feature.auth.enums.Status;
import com.mrs.enpoint.feature.auth.mapper.AuthMapper;
import com.mrs.enpoint.feature.auth.mapper.UserMapper;
import com.mrs.enpoint.feature.auth.repository.RevokedTokenRepository;
import com.mrs.enpoint.feature.auth.repository.RoleRepository;
import com.mrs.enpoint.feature.auth.repository.UserRepository;
import com.mrs.enpoint.feature.recharge.enums.ConnectionStatus;
import com.mrs.enpoint.feature.recharge.repository.MobileConnectionRepository;
import com.mrs.enpoint.shared.exception.BusinessException;
import com.mrs.enpoint.shared.exception.DuplicateAlreadyExistsException;
import com.mrs.enpoint.shared.exception.InvalidCredentialsException;
import com.mrs.enpoint.shared.exception.MobileNotRegisteredException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.exception.SamePasswordException;
import com.mrs.enpoint.shared.exception.TokenExpiredException;
import com.mrs.enpoint.shared.exception.UserAccessException;
import com.mrs.enpoint.shared.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final AuditService auditService;
	private final RevokedTokenRepository revokedTokenRepository;
	private final MobileConnectionRepository mobileConnectionRepository;

	public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuditService auditService, RevokedTokenRepository revokedTokenRepository, MobileConnectionRepository mobileConnectionRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.auditService = auditService;
		this.revokedTokenRepository = revokedTokenRepository;
		this.mobileConnectionRepository = mobileConnectionRepository;
	}

	@Override
	@Transactional
	public void register(RegisterRequestDTO request) {

		String email = request.getEmail().trim().toLowerCase();
		
		// mobile number check is there in mobile connection
		if(!mobileConnectionRepository.existsByMobileNumberAndStatus(request.getMobileNumber(), ConnectionStatus.ACTIVE)) {
			throw new MobileNotRegisteredException("Mobile number " + request.getMobileNumber() + " is not registered on this platform.");
		}

		
		if (userRepository.existsByEmail(email)) {
			throw new DuplicateAlreadyExistsException("Email already registered");
		}

		if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
			throw new DuplicateAlreadyExistsException("Mobile number already registered");
		}

		Role role = roleRepository.findByRoleName(RoleType.USER)
				.orElseThrow(() -> new NotFoundException("USER role not found"));

		User user = new User();
		user.setFullName(request.getFullName());
		user.setGender(request.getGender());
		user.setEmail(email);
		user.setMobileNumber(request.getMobileNumber());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(role);
		user.setStatus(Status.ACTIVE);
		user.setCreatedAt(LocalDateTime.now());

		User saved = userRepository.save(user);
		auditService.log(saved.getUserId(), EntityName.USER, saved.getUserId(), AuditAction.USER_REGISTRATION, null,
				"User registered: " + saved.getEmail());
	}

	@Override
	public AuthResponseDTO login(String email, String password) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidCredentialsException("Invalid credentials");
		}

		if (user.getStatus() == Status.INACTIVE) {
			throw new UserAccessException("Account is inactive");
		}

		if (user.getStatus() == Status.BLOCKED) {
			throw new UserAccessException("Account is blocked");
		}

		String role = user.getRole().getRoleName().name();
		String accessToken = jwtUtil.generateToken(user.getEmail(), role);
		String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

		return AuthMapper.toAuthResponseDTO(user, accessToken, refreshToken);
	}

	@Override
	public AuthResponseDTO refreshAccessToken(String refreshToken) {

		if (!jwtUtil.isTokenValid(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
			throw new InvalidCredentialsException("Invalid or expired refresh token");
		}

		String email = jwtUtil.extractEmail(refreshToken);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new InvalidCredentialsException("User not found"));

		if (user.getStatus() == Status.INACTIVE || user.getStatus() == Status.BLOCKED) {
			throw new UserAccessException("Account is not active");
		}

		String role = user.getRole().getRoleName().name();
		String newAccessToken = jwtUtil.generateToken(email, role);
		String newRefreshToken = jwtUtil.generateRefreshToken(email); // rotate refresh token

		return AuthMapper.toAuthResponseDTO(user, newAccessToken, newRefreshToken);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Transactional
	public void changePassword(ChangePasswordRequestDTO request) {

		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Current password is incorrect");
		}

		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new BusinessException("New password and confirm password do not match");
		}

		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
			throw new SamePasswordException("New password must be different from current password");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);

		auditService.log(user.getUserId(), EntityName.USER, user.getUserId(), AuditAction.PASSWORD_CHANGE, null,
				"Password changed for: " + email);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@Transactional
	public UserResponseDTO updateProfile(UpdateProfileRequestDTO request) {

		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

//		// check mobile conflict only if it changed
//		if (!user.getMobileNumber().equals(request.getMobileNumber())
//				&& userRepository.existsByMobileNumber(request.getMobileNumber())) {
//			throw new DuplicateAlreadyExistsException("Mobile number already in use");
//		}

		user.setFullName(request.getFullName());
		user.setGender(request.getGender());
//		user.setMobileNumber(request.getMobileNumber());

		return UserMapper.toResponseDTO(userRepository.save(user));
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public UserResponseDTO getProfileDetails() {
		
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

	    return UserMapper.toResponseDTO(user);
	}

	@Override
	public void logout(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");
	    
	    // check if the header is missing or doesn't start with Bearer
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        throw new NotFoundException("No valid authorization token found.");
	    }

	    String token = authHeader.substring(7);
	         
	    // check if token is ALREADY revoked
	    if (revokedTokenRepository.existsByToken(token)) {
	        throw new TokenExpiredException("Token is already revoked and user is already logged out.");
	    }
	    
	    // if valid and not revoked, save it to the blacklist
	    revokedTokenRepository.save(new RevokedToken(token));
	    
	    SecurityContextHolder.clearContext();
	}
	
	
}
