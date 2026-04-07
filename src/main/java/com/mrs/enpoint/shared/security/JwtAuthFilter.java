package com.mrs.enpoint.shared.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mrs.enpoint.feature.auth.repository.RevokedTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RevokedTokenRepository revokedTokenRepository;
	
	public JwtAuthFilter(JwtUtil jwtUtil, RevokedTokenRepository revokedTokenRepository) {
		this.jwtUtil = jwtUtil;
		this.revokedTokenRepository = revokedTokenRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			
			boolean isRevoked = revokedTokenRepository.existsByToken(token);
			
			if(jwtUtil.isTokenValid(token) && !isRevoked) {
				String email = jwtUtil.extractEmail(token);
				String role = jwtUtil.extractRole(token);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						email, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
}
	