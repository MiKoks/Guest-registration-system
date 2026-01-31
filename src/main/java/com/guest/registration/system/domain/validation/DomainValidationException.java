package com.guest.registration.system.domain.validation;

/**
 * Exception for domain/business rule violations.
 */
public class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }
}
