package com.mrs.enpoint.feature.operator.exception;

public class DuplicateOperatorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateOperatorException(String message) {
        super(message);
    }
}
