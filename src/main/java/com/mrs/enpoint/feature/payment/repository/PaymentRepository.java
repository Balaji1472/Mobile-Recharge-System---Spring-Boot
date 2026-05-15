package com.mrs.enpoint.feature.payment.repository;

import com.mrs.enpoint.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findTopByRechargeTransaction_RechargeIdOrderByAttemptNumberDesc(int rechargeId);
    
    Optional<Payment> findByTransactionReference(String transactionReference);

}