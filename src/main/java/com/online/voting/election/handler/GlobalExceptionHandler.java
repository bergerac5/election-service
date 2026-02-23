package com.online.voting.election.handler;

import org.apache.coyote.BadRequestException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.online.voting.election.dtos.ApiResponse;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Handle domain ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ex.getMessage(), null));
    }

    // ✅ Handle domain AlreadyExistsException
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(ex.getMessage(), null));
    }

    // ✅ Handle domain BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(ex.getMessage(), null));
    }

    // ✅ Handle Feign 404 errors from Candidate Service
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ApiResponse<Object>> handleFeignNotFound(FeignException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("Candidate not found", null));
    }

    // ✅ Handle DataIntegrityViolationException for duplicate candidate assignment
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>("Candidate already assigned to this position", null));
    }

    // ✅ Handle Feign 403 errors from Candidate Service
    @ExceptionHandler(FeignException.Forbidden.class)
    public ResponseEntity<ApiResponse<Object>> handleFeignForbidden(
            FeignException.Forbidden ex) {

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>("Access denied when verifying candidate", null));
    }

    // ✅ Catch all for other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        ex.printStackTrace(); // TEMP: see real cause in console
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Oops! Something went wrong: " + ex, null));
    }
}
