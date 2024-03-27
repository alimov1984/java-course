package ru.alimov.limitservice.demo.exception;

public class LimitServiceException extends RuntimeException {
    private final String code;

    public LimitServiceException(String code) {
        this.code = code;
    }

    public LimitServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public LimitServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
