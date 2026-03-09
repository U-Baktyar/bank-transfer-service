package com.aiylbank.backend.exception;

public class AccountStatusException extends RuntimeException{
    public AccountStatusException() {
        super();
    }

    public AccountStatusException(String message) {
        super(message);
    }

    public AccountStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
