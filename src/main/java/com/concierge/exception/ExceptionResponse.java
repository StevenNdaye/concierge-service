package com.concierge.exception;

public class ExceptionResponse {
    private int errorCode;
    private String errorMessage;

    ExceptionResponse() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
