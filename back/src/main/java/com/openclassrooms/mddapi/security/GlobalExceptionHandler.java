package com.openclassrooms.mddapi.security;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler for the MDD API application.
 * 
 * This controller advice provides centralized exception handling across all
 * REST controllers in the application. It intercepts exceptions thrown by
 * controllers and transforms them into appropriate HTTP responses with
 * consistent error message formatting.
 * 
 * The handler ensures that clients receive well-formed error responses with
 * appropriate HTTP status codes and error messages, improving API consistency
 * and error handling across the application.
 * 
 * @author Cécile UMECKER
 
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResponseStatusException thrown by controllers or services.
     * 
     * This method intercepts ResponseStatusException instances and converts them
     * into properly formatted HTTP responses. The response includes the HTTP status
     * code from the exception and a JSON body containing the error message.
     * 
     * Response format: {"error": "error message"}
     * 
     * @param ex the ResponseStatusException to handle
     * @return ResponseEntity containing the error message and appropriate HTTP status
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> body = Map.of("error", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}

