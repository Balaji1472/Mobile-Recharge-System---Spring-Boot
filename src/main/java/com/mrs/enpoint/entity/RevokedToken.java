package com.mrs.enpoint.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "revoked_tokens")
public class RevokedToken {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    private LocalDateTime revokedAt;

    public RevokedToken() {
    	
    }
    
    public RevokedToken(String token) {
        this.token = token;
        this.revokedAt = LocalDateTime.now();
    }
    
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiryDate() {
		return revokedAt;
	}

	public void setExpiryDate(LocalDateTime revokedAt) {
		this.revokedAt = revokedAt;
	}
    
    
}
