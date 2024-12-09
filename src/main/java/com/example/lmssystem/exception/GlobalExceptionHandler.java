package com.example.lmssystem.exception;

import com.example.lmssystem.transfer.AppErrorResponse;
import com.example.lmssystem.transfer.ResponseData;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Ushbu amalni bajarishga sizning huquqingiz yo'q", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<AppErrorResponse> handleBadRequestException(BadRequestException ex) {
        AppErrorResponse errorResponse = new AppErrorResponse();
        errorResponse.setStatus(ex.getBody().getStatus());
        errorResponse.setError(ex.getReason());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        AppErrorResponse errorResponse = new AppErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);  // Log the error for internal monitoring
        ResponseData errorResponse = ResponseData.builder()
                .success(false)
                .data(null)
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Avoid 500
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseData> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseData errorResponse = ResponseData.builder()
                .success(false)
                .data(null)
                .message("Invalid input: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<ResponseData> handlePermissionNotFoundException(PermissionNotFoundException ex) {
        ResponseData errorResponse = ResponseData.builder()
                .success(false)
                .data(null)
                .message("Permission not found: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericServiceException.class)
    public ResponseEntity<String> handleGenericServiceException(GenericServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseData> handleIllegalStateException(IllegalStateException ex) {
        ResponseData errorResponse = ResponseData.builder()
                .success(false)
                .data(null)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<String> handleUserServiceException(UserServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Null pointer exception occurred: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}