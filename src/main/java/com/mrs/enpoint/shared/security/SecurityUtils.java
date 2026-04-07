package com.mrs.enpoint.shared.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mrs.enpoint.feature.auth.repository.UserRepository;

@Component
public class SecurityUtils {

	private final UserRepository userRepository;

	public SecurityUtils(UserRepository userRepository) {
		this.userRepository = userRepository;
	}  
	
	public int getCurrentUserId() {
		String email = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal().toString();
		return userRepository.findByEmail(email).map(user -> user.getUserId()).orElse(0); 
				
	}
}

  