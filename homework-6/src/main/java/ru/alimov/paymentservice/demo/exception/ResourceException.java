package ru.alimov.paymentservice.demo.exception;

public class ResourceException extends RuntimeException {
    private String code;

    public ResourceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ResourceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
