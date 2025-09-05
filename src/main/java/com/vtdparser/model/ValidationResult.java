package com.vtdparser.model;

public class ValidationResult {
    private boolean valid;
    private String message;

    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", message='" + message + '\'' +
                '}';
    }
}