package com.aiylbank.backend.exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super();
    }

    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
