package com.mrs.enpoint.feature.refund.repository;

import com.mrs.enpoint.entity.Refund;
import com.mrs.enpoint.feature.refund.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Integer> {


    List<Refund> findByPayment_RechargeTransaction_User_UserId(int userId);

    long countByStatus(RefundStatus status);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Refund r WHERE r.status = 'PROCESSED'")
    BigDecimal sumProcessedRefundAmounts();
}