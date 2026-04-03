package com.mrs.enpoint.feature.planoffer.exception;

public class DuplicatePlanOfferException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicatePlanOfferException(String message) {
        super(message);
    }
}