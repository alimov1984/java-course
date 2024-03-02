package ru.alimov.paymentservice.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alimov.paymentservice.demo.dto.ErrorPaymentDto;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String RESOURCE_NOT_AVALAIBLE = "RESOURCE_NOT_AVALAIBLE";
    private static final String RESOURCE_PROCESS_ERROR = "RESOURCE_PROCESS_ERROR";
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String PRODUCT_INSUFFICIENT_FUNDS = "PRODUCT_INSUFFICIENT_FUNDS";
    private static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";

    @ExceptionHandler(ProductUnavailableException.class)
    public ResponseEntity<ErrorPaymentDto> handlerProductUnavailableException(ProductUnavailableException ex) {
        return prepareErrorResponse(RESOURCE_NOT_AVALAIBLE, ex.getMessage());
    }

    @ExceptionHandler(ProductProcessException.class)
    public ResponseEntity<ErrorPaymentDto> handlerProductProcessException(ProductProcessException ex) {
        return prepareErrorResponse(RESOURCE_PROCESS_ERROR, ex.getMessage());
    }

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ErrorPaymentDto> handlerPaymentValidationException(PaymentValidationException ex) {
        return prepareErrorResponse(VALIDATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ProductInsufficientFundsException.class)
    public ResponseEntity<ErrorPaymentDto> handlerProductInsufficientFundsException(ProductInsufficientFundsException ex) {
        return prepareErrorResponse(PRODUCT_INSUFFICIENT_FUNDS, ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorPaymentDto> handlerProductNotFoundException(ProductNotFoundException ex) {
        return prepareErrorResponse(PRODUCT_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorPaymentDto> handlerUserNotFoundException(UserNotFoundException ex) {
        return prepareErrorResponse(USER_NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<ErrorPaymentDto> prepareErrorResponse(String code, String message) {
        return new ResponseEntity<>(new ErrorPaymentDto(code, message), HttpStatus.BAD_REQUEST);
    }

}
