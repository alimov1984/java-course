package ru.alimov.paymentservice.demo.exception;

public class ProductServiceProcessException extends RuntimeException {
    public ProductServiceProcessException() {
    }

    public ProductServiceProcessException(String message) {
        super(message);
    }

    public ProductServiceProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
