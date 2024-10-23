package com.example.lmssystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import uz.olmossoft.crmproject.model.transfer.AppErrorResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return "Ushbu amalni bajarishga sizning huquqingiz yo'q";
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<AppErrorResponse> handleAccessDeniedException(BadRequestException ex) {
        AppErrorResponse errorResponse = new AppErrorResponse();
        errorResponse.setStatus(ex.getBody().getStatus());
        errorResponse.setError(ex.getReason());
        errorResponse.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        AppErrorResponse errorResponse = new AppErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        log.error(ex.getMessage(), ex);
        AppErrorResponse errorResponse = new AppErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
