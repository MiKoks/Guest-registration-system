package com.guest.registration.system.presentation.exceptions;

import com.guest.registration.system.domain.validation.DomainValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ApiError> handleDomainValidation(DomainValidationException ex, HttpServletRequest req) {
        HttpStatus status = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("not found")
            ? HttpStatus.NOT_FOUND
            : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(new ApiError(
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(),
            req.getRequestURI()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.joining("; "));

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ApiError(
            status.value(),
            status.getReasonPhrase(),
            msg.isBlank() ? "Validation failed" : msg,
            req.getRequestURI()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        // Nt uq_event_participant või unikaalsed koodid (payment_methods.code, registry_code, personal_code)
        HttpStatus status = HttpStatus.CONFLICT;

        String msg = "Data integrity violation";
        if (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null) {
            String lower = ex.getMostSpecificCause().getMessage().toLowerCase();
            if (lower.contains("uq_event_participant") || lower.contains("duplicate") || lower.contains("unique")) {
                msg = "Duplicate or conflicting data";
            }
        }

        return ResponseEntity.status(status).body(new ApiError(
            status.value(),
            status.getReasonPhrase(),
            msg,
            req.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new ApiError(
            status.value(),
            status.getReasonPhrase(),
            "Unexpected error",
            req.getRequestURI()
        ));
    }
}
