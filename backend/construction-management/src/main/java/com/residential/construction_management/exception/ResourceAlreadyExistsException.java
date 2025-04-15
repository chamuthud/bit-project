package com.residential.construction_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Maps this exception to HTTP 409 Conflict
public class ResourceAlreadyExistsException extends RuntimeException {

    // Constructor with message
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor for convenience when checking unique fields
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}