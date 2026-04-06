package com.mrs.enpoint.feature.invoice.repository;

import com.mrs.enpoint.entity.RechargeInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<RechargeInvoice, Integer> {

    // Get invoice by recharge id
    Optional<RechargeInvoice> findByRechargeTransaction_RechargeId(int rechargeId);

    // All invoices belonging to a specific user (via recharge -> user)
    List<RechargeInvoice> findByRechargeTransaction_User_UserId(int userId);
}