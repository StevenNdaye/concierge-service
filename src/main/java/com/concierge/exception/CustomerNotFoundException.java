package com.concierge.exception;

public class CustomerNotFoundException extends RuntimeException {
    private Long id;

    public CustomerNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
