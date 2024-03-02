package ru.alimov.paymentservice.demo.exception;

public class ProductProcessException extends RuntimeException {
    public ProductProcessException() {
    }

    public ProductProcessException(String message) {
        super(message);
    }

    public ProductProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
