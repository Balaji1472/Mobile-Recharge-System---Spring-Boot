package com.mrs.enpoint.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recharge_invoice")
public class RechargeInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private int invoiceId;

    // One invoice per recharge
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recharge_id", nullable = false, unique = true)
    private RechargeTransaction rechargeTransaction;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    public RechargeInvoice() {}

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public RechargeTransaction getRechargeTransaction() {
        return rechargeTransaction;
    }

    public void setRechargeTransaction(RechargeTransaction rechargeTransaction) {
        this.rechargeTransaction = rechargeTransaction;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}