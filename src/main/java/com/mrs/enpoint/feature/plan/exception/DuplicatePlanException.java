package com.mrs.enpoint.feature.plan.exception;

public class DuplicatePlanException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicatePlanException(String message) {
        super(message);
    }
}