package com.example.dqc.api.common;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorWrapper(ErrorDetails error) {}
    public record ErrorDetails(String code, String message) {}

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorWrapper> handleValidation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorWrapper(new ErrorDetails("VALIDATION_ERROR", ex.getMessage())));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorWrapper> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorWrapper(new ErrorDetails("BAD_REQUEST", "Некорректные параметры запроса")));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorWrapper> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorWrapper(new ErrorDetails("VALIDATION_ERROR", ex.getMessage())));
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorWrapper> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorWrapper(new ErrorDetails("NOT_FOUND", ex.getMessage())));
    }
}
