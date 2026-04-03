package com.mrs.enpoint.feature.planoffer.exception;

public class PlanOfferNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlanOfferNotFoundException(String message) {
        super(message);
    }
}