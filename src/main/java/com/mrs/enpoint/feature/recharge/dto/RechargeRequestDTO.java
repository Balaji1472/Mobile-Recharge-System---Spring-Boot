package com.mrs.enpoint.feature.recharge.dto;

import com.mrs.enpoint.feature.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RechargeRequestDTO {

    @NotNull(message = "Connection ID is required")
    @Positive(message = "Connection ID must be positive")
    private int connectionId;

    @NotNull(message = "Plan ID is required")
    @Positive(message = "Plan ID must be positive")
    private int planId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}