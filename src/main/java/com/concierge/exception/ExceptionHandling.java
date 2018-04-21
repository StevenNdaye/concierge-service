package com.concierge.exception;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandling {

    private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error+json");

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<VndErrors> resourceNotFound(CityNotFoundException ex) {
        return this.error(ex, HttpStatus.NOT_FOUND, ex.getCityName() + "");
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<VndErrors> customerNotFound(CustomerNotFoundException ex) {
        return this.error(ex, HttpStatus.NOT_FOUND, ex.getId() + "");
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<VndErrors> serviceUnavailable(ServiceUnavailableException ex) {
        return this.error(ex, HttpStatus.EXPECTATION_FAILED, ex.getCityName() + "");
    }

    private <E extends Exception> ResponseEntity<VndErrors> error(E error, HttpStatus httpStatus, String logRef) {
        String msg = Optional.of(error.getMessage())
                .orElse(error.getClass().getSimpleName());

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(this.vndErrorMediaType);

        return new ResponseEntity<>(new VndErrors(logRef, msg), httpHeaders, httpStatus);
    }
}
