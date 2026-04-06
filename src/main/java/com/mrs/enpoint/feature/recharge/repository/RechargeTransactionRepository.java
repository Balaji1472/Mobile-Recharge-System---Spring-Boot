package com.mrs.enpoint.feature.recharge.repository;

import com.mrs.enpoint.entity.RechargeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RechargeTransactionRepository extends JpaRepository<RechargeTransaction, Integer> {

    // All recharges initiated by a specific user (own + others)
    List<RechargeTransaction> findByUser_UserId(int userId);
}