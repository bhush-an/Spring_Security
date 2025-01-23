package com.app.global_exc;

import com.app.dto.ResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errorMap = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.status(status.value()).body(errorMap);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        Map<String, String> errorMap = ex.getFieldErrors().stream()
//                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Handle cases like unique constraint violation
        String msg = ex.getMostSpecificCause().getMessage();
        ResponseDTO errorResponse;
        if (msg.contains("Duplicate entry")) {
            errorResponse = ResponseDTO.builder()
                    .message("Data integrity violation: " + "Username already exists!")
                    .build();
        } else {
            errorResponse = ResponseDTO.builder()
                    .message("Data integrity violation: " + ex.getMostSpecificCause().getMessage())
                    .build();
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        // Handle validation constraint violations (e.g., @NotNull, @Size, etc.)
        ResponseDTO errorResponse = ResponseDTO.builder()
                .message("Validation failed: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseDTO.builder()
                        .message("Request Header Missing!")
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO.builder().message(e.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDTO.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.valueOf(401))
                .body(ResponseDTO.builder()
                        .message("Authentication Failed!")
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.valueOf(403))
                .body(ResponseDTO.builder()
                        .message("Access Denied!")
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(SignatureException e) {
        return ResponseEntity.status(HttpStatus.valueOf(404))
                .body(ResponseDTO.builder()
                        .message("Invalid JWT!")
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.valueOf(404))
                .body(ResponseDTO.builder()
                        .message("JWT Expired!")
                        .errorMessage(e.getMessage())
                        .build());
    }
}
