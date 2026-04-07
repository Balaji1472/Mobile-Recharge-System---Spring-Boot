package com.mrs.enpoint.feature.recharge.repository;

import com.mrs.enpoint.entity.RechargeTransaction;
import com.mrs.enpoint.feature.recharge.enums.RechargeStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface RechargeTransactionRepository extends JpaRepository<RechargeTransaction, Integer> {

    
    List<RechargeTransaction> findByUser_UserId(int userId);
    
    long countByStatus(RechargeStatus status);
 
    long countByUser_UserIdAndStatus(int userId, RechargeStatus status);
 
    @Query("SELECT COALESCE(SUM(r.finalAmount), 0) FROM RechargeTransaction r WHERE r.status = 'SUCCESS'")
    BigDecimal sumSuccessfulRechargeAmounts();
 
    @Query("SELECT r FROM RechargeTransaction r WHERE r.user.userId = ?1 AND r.status = 'SUCCESS' ORDER BY r.completedAt DESC")
    List<RechargeTransaction> findLatestSuccessRechargeByUser(int userId);

}