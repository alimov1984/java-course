package ru.alimov.paymentservice.demo.exception;

public class ProductInsufficientFundsException extends RuntimeException {
    public ProductInsufficientFundsException() {
    }

    public ProductInsufficientFundsException(String message) {
        super(message);
    }

    public ProductInsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
