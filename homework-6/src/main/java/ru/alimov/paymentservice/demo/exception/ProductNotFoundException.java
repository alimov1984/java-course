package ru.alimov.paymentservice.demo.exception;

public class PaymentProductNotFoundException extends RuntimeException {
    public PaymentProductNotFoundException() {
    }

    public PaymentProductNotFoundException(String message) {
        super(message);
    }

    public PaymentProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
