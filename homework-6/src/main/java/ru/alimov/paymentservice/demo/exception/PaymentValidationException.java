package ru.alimov.paymentservice.demo.exception;

public class PaymentServiceValidationException extends RuntimeException {
    public PaymentServiceValidationException() {
    }

    public PaymentServiceValidationException(String message) {
        super(message);
    }

    public PaymentServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
