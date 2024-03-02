package ru.alimov.paymentservice.demo.exception;

public class PaymentInsufficientFundsException extends RuntimeException {
    public PaymentInsufficientFundsException() {
    }

    public PaymentInsufficientFundsException(String message) {
        super(message);
    }

    public PaymentInsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
