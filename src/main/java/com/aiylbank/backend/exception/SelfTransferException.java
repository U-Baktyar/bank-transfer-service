package com.aiylbank.backend.exception;

public class SelfTransferException extends RuntimeException {
    public SelfTransferException() {
        super();
    }

    public SelfTransferException(String message) {
        super(message);
    }

    public SelfTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
