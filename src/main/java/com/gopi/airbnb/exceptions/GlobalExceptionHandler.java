package com.gopi.airbnb.exceptions;

import com.gopi.airbnb.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse>handleResourceAlreadyExists(ResourceAlreadyExistsException exception){
        ErrorResponse errorResponse= new ErrorResponse(HttpStatus.CONFLICT.value(),exception.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse>handleInvalidCredentials(InvalidCredentialsException exception){
        ErrorResponse errorResponse= new ErrorResponse(HttpStatus.NOT_FOUND.value(),exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse>handleUserNameNotFound(UsernameNotFoundException exception){
        ErrorResponse errorResponse= new ErrorResponse(HttpStatus.NOT_FOUND.value(),exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>handleInvalidRequests(MethodArgumentNotValidException exception){
        ErrorResponse errorResponse= new ErrorResponse(HttpStatus.BAD_REQUEST.value(),exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>handleResourceNotFundException(ResourceNotFoundException exception){
        ErrorResponse errorResponse= new ErrorResponse(HttpStatus.NOT_FOUND.value(),exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse>handleRuntimeException(RuntimeException exception){
        ErrorResponse errorResponse= new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
