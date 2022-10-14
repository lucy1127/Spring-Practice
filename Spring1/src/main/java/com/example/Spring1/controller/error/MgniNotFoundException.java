package com.example.Spring1.controller.error;

public class MgniNotFoundException extends RuntimeException{
    private final String errorMessage;

    public MgniNotFoundException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
