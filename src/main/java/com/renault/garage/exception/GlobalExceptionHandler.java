package com.renault.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;


/**
 * Global exception handler that captures and processes all custom exceptions across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException globally.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles StorageLimitExceededException globally.
     */
    @ExceptionHandler(StorageLimitExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleStorageLimitExceeded(StorageLimitExceededException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles any unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        return buildErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Builds a formatted error response.
     */
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(String message, HttpStatus status, WebRequest request) {

        ErrorResponseDTO errorDetails = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(String.valueOf(status.value()))
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorDetails, status);
    }
}
