package com.mrs.enpoint.feature.plan.exception;

public class PlanNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlanNotFoundException(String message) {
        super(message);
    }
}