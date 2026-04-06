package com.mrs.enpoint.shared.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- helper Method ---
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    // --- exception handlers ---

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }
  
    @ExceptionHandler(DuplicateAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", ex.getMessage());
    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
//    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Business Error", ex.getMessage());
    }

//    @ExceptionHandler(DuplicateAlreadyExistsException.class)
//    public ResponseEntity<Map<String, Object>> handleDuplicateCategory(DuplicateAlreadyExistsException ex) {
//        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", "Category code already exists");
//    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleUserNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
//    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", ex.getMessage());
    }
    
    @ExceptionHandler(UserAccessException.class)
    public ResponseEntity<Map<String, Object>> handleUseAccess(UserAccessException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Access Issue", ex.getMessage());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleInputViolation(ConstraintViolationException ex){
    	return buildError(HttpStatus.BAD_REQUEST, "Constraint Violation", ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Unauthorized access", ex.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(AccessDeniedException ex) {
        return buildError(HttpStatus.FORBIDDEN, "Access Denied", ex.getMessage());
    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> offerNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
//    }

//    @ExceptionHandler(DuplicateAlreadyExistsException.class)
//    public ResponseEntity<Map<String, Object>> handleOfferAlreadyExists(DuplicateAlreadyExistsException ex) {
//        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", ex.getMessage());
//    }

    @ExceptionHandler(FailedToLogException.class)
    public ResponseEntity<Map<String, Object>> handleFailedToStoreAuditLog(FailedToLogException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", "Failed to log");
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleLogNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
//    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handlePlanNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
//    }

//    @ExceptionHandler(DuplicateAlreadyExistsException.class)
//    public ResponseEntity<Map<String, Object>> handlePlanAlreadyExists(DuplicateAlreadyExistsException ex) {
//        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", ex.getMessage());
//    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<Map<String, Object>> handleSamePassword(SamePasswordException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Invalid Request", ex.getMessage());
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handlePlanOfferNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
//    }

//    @ExceptionHandler(DuplicateAlreadyExistsException.class)
//    public ResponseEntity<Map<String, Object>> handleDuplicatePlanOffer(DuplicateAlreadyExistsException ex) {
//        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", ex.getMessage());
//    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
//        return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
//    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpired(TokenExpiredException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Token Expired", ex.getMessage());
    }
    
    @ExceptionHandler(MobileNotRegisteredException.class)
    public ResponseEntity<Map<String, Object>> handleMobileNotRegistered(MobileNotRegisteredException ex) {
        return buildError(HttpStatus.FORBIDDEN, "Mobile Not Registered", ex.getMessage());
    }
    
//    @ExceptionHandler(DuplicateAlreadyExistsException.class)
//    public ResponseEntity<Map<String, Object>> handleDuplicateSavedNumber(DuplicateAlreadyExistsException ex) {
//        return buildError(HttpStatus.CONFLICT, "Duplicate Entry", ex.getMessage());
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach((FieldError fe) -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

        Map<String, Object> error = new LinkedHashMap<>(); // Consistent order
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Failed");
        error.put("fields", fieldErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }
}