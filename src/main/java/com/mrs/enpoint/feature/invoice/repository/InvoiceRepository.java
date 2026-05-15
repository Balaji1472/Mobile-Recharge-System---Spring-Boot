package com.mrs.enpoint.feature.invoice.repository;

import com.mrs.enpoint.entity.RechargeInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<RechargeInvoice, Integer> {

    Optional<RechargeInvoice> findByRechargeTransaction_RechargeId(int rechargeId);

    List<RechargeInvoice> findByRechargeTransaction_User_UserId(int userId);
}