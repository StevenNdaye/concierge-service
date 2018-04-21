package com.concierge.exception;


public class ServiceUnavailableException extends RuntimeException {

    private String name;

    public ServiceUnavailableException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getCityName() {
        return name;
    }
}
