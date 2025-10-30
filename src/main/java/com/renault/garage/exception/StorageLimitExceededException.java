package com.renault.garage.exception;

public class StorageLimitExceededException extends RuntimeException {
    public StorageLimitExceededException(String message) {
        super(message);
    }
}
