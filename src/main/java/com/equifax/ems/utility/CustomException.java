package com.equifax.ems.utility;

public class CustomException extends RuntimeException {
    private final String errorMessage;

    public CustomException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
