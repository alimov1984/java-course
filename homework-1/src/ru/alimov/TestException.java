package ru.alimov;

public class TestException extends RuntimeException {
    public TestException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestException(String message) {
        super(message);
    }
}
