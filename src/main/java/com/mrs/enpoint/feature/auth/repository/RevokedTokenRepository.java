package com.mrs.enpoint.feature.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrs.enpoint.entity.RevokedToken;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Integer>{

	boolean existsByToken(String token);
}
