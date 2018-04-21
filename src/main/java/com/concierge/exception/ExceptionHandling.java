package com.concierge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(CityNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.NOT_FOUND.value());
        response.setErrorMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> customerNotFound(CustomerNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.NOT_FOUND.value());
        response.setErrorMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionResponse> serviceUnavailable(ServiceUnavailableException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.EXPECTATION_FAILED.value());
        response.setErrorMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }
}
