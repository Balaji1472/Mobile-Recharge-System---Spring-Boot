package com.mrs.enpoint.feature.recharge.repository;

import com.mrs.enpoint.entity.MobileConnection;
import com.mrs.enpoint.feature.recharge.enums.ConnectionStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MobileConnectionRepository extends JpaRepository<MobileConnection, Integer> {

    Optional<MobileConnection> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumberAndStatus(String mobileNumber, ConnectionStatus status);
}