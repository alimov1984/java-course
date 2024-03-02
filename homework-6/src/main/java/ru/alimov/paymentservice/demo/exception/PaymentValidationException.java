package ru.alimov.paymentservice.demo.exception;

public class PaymentValidationException extends RuntimeException {
    public PaymentValidationException() {
    }

    public PaymentValidationException(String message) {
        super(message);
    }

    public PaymentValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
