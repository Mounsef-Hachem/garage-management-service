package com.renault.garage.exception;

public class GlobalExceptionHandler extends RuntimeException {
    public GlobalExceptionHandler(String message) {
        super(message);
    }
}
