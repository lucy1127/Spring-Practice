package com.example.project.controller.error;

public class InvalidPropertiesFormatException extends RuntimeException{

    private final String errorMessage;

    public InvalidPropertiesFormatException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
}
