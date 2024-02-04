package ru.alimov.homework2;

public class EmptyResultException extends RuntimeException {
    public EmptyResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyResultException(String message) {
        super(message);
    }
}
