package com.mrs.enpoint.shared.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long expiration;
	
	@Value("${jwt.refresh-expiration}")
	private long refreshExpiration;
	
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(String email, String role) {
		return Jwts.builder()
				.subject(email)
				.claim("role", role)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey())
				.compact();
	}
	
	public String generateRefreshToken(String email) {
		return Jwts.builder()
				.subject(email)
				.claim("type", "refresh")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + refreshExpiration))
				.signWith(getSigningKey())
				.compact();
	}
	
	public String extractEmail(String token) {
		return parseClaims(token).getSubject();
	}
	
	public String extractRole(String token) {
		return parseClaims(token).get("role", String.class);
	}
	
	public boolean isTokenValid(String token) {
		try {
			parseClaims(token);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public boolean isRefreshToken(String token) {
		try {
			String type = parseClaims(token).get("type", String.class);
			return "refresh".equals(type);
		}
		catch(Exception e) {
			return false;
		}
	}
	 
	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
