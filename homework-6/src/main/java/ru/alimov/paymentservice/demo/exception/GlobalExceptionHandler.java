package ru.alimov.paymentservice.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alimov.paymentservice.demo.dto.ErrorPaymentDto;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String PRODUCT_INSUFFICIENT_FUNDS = "PRODUCT_INSUFFICIENT_FUNDS";

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ErrorPaymentDto> handlerResourceException(ResourceException ex) {
        return prepareErrorResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ErrorPaymentDto> handlerPaymentValidationException(PaymentValidationException ex) {
        return prepareErrorResponse(VALIDATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ProductInsufficientFundsException.class)
    public ResponseEntity<ErrorPaymentDto> handlerProductInsufficientFundsException(ProductInsufficientFundsException ex) {
        return prepareErrorResponse(PRODUCT_INSUFFICIENT_FUNDS, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorPaymentDto> handlerEntityNotFoundException(EntityNotFoundException ex) {
        return prepareErrorResponse(ex.getCode(), ex.getMessage());
    }

    private ResponseEntity<ErrorPaymentDto> prepareErrorResponse(String code, String message) {
        return new ResponseEntity<>(new ErrorPaymentDto(code, message), HttpStatus.BAD_REQUEST);
    }

}
