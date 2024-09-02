package com.equifax.ems.utility;

public class ValidationException extends RuntimeException {
    private String errorMessage;

    public ValidationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
