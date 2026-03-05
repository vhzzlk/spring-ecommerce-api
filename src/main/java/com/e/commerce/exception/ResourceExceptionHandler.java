package com.e.commerce.exception;

import com.e.commerce.dto.error.StandardError;
import com.e.commerce.dto.error.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Handler global de exceções da aplicação.
 *
 * Intercepta todas as exceptions lançadas pelos controllers/services
 * e retorna respostas HTTP padronizadas com DTOs de erro.
 *
 * Handlers disponíveis:
 * 1. ResourceNotFoundException → 404
 * 2. DatabaseException → 409
 * 3. UnauthorizedException → 401
 * 4. ForbiddenException → 403
 * 5. MethodArgumentNotValidException → 422
 * 6. DataIntegrityViolationException → 409
 * 7. Exception (fallback) → 500
 */
@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        String error = "Database error";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> unauthorized(UnauthorizedException e, HttpServletRequest request) {
        String error = "Unauthorized";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardError> forbidden(ForbiddenException e, HttpServletRequest request) {
        String error = "Forbidden";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Validation error";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(), error, "Erro de validacao", request.getRequestURI());

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        String error = "Database integrity violation";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, "Violacao de integridade dos dados", request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> unexpectedError(Exception e, HttpServletRequest request) {
        String error = "Unexpected error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}

