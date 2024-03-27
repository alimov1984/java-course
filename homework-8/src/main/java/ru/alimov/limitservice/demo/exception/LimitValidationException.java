package ru.alimov.limitservice.demo.exception;

public class LimitValidationException extends RuntimeException {
    private final String code;

    public LimitValidationException(String code) {
        this.code = code;
    }

    public LimitValidationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public LimitValidationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
