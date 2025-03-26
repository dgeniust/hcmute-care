package vn.edu.hcmute.utecare.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        log.warn("Invalid argument: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Argument", e.getMessage(), request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e, WebRequest request) {
        String errorMessage;
        String error;

        if (e instanceof MethodArgumentNotValidException ex) {
            error = "Invalid input data";
            errorMessage = ex.getBindingResult().getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            error = "Invalid path variable";
            errorMessage = e.getMessage().substring(e.getMessage().indexOf(" ") + 1);
        }

        log.warn("Validation error: {}", errorMessage);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, error, errorMessage, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        log.warn("Resource not found: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage(), request);
    }

    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedException(DuplicatedException e, WebRequest request) {
        log.warn("Duplicate data: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage(), request);
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDataException(InvalidDataException e, WebRequest request) {
        log.warn("Invalid data: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), request);
    }

    @ExceptionHandler({ForBiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        log.warn("Access denied: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAuthenticationException(AuthenticationException e, WebRequest request) {
        log.warn("Authentication failed: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase(), "Authentication failed", request);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataAccessException(DataAccessException e, WebRequest request) {
        log.error("Data access error: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        log.error("Data integrity error: {}", e.getCause().getMessage(), e);
        return buildErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase(), e.getCause().getMessage(), request);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSQLException(SQLException e, WebRequest request) {
        String detailedMessage = String.format("Database error: %s (SQL State: %s, Error Code: %d)",
                e.getMessage(), e.getSQLState(), e.getErrorCode());
        log.error("SQL error: {}", detailedMessage, e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Database query error", detailedMessage, request);
    }

    @ExceptionHandler(TooManyOtpRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleTooManyOtpRequestsException(TooManyOtpRequestsException e, WebRequest request) {
        log.warn("Too many OTP requests: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", e.getMessage(), request);
    }

    @ExceptionHandler(RedisOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRedisOperationException(RedisOperationException e, WebRequest request) {
        log.error("Redis operation failed: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Redis Operation Error", e.getMessage(), request);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralException(Exception e, WebRequest request) {
        log.error("Unknown error: {}", e.getMessage(), e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), request);
    }



    private ErrorResponse buildErrorResponse(HttpStatus status, String error, String message, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(new Date())
                .status(status.value())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(error)
                .message(message)
                .build();
    }
}