package ru.alimov.limitservice.demo.exception;

public class LockNotAcquiredException extends RuntimeException{
    public LockNotAcquiredException() {
    }

    public LockNotAcquiredException(String message) {
        super(message);
    }

    public LockNotAcquiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
