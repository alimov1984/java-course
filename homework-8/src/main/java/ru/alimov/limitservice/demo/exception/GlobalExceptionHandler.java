package ru.alimov.limitservice.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alimov.limitservice.demo.dto.LimitErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(LimitServiceException.class)
    public ResponseEntity<LimitErrorDto> handlerLimitServiceException(LimitServiceException ex) {
        logger.error(ex.getCode(), ex);
        return toBadRequestResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<LimitErrorDto> handlerEntityNotFoundException(EntityNotFoundException ex) {
        logger.error(ex.getCode(), ex);
        return toNotFoundResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(LimitValidationException.class)
    public ResponseEntity<LimitErrorDto> handlerLimitValidationException(LimitValidationException ex) {
        logger.error(ex.getCode(), ex);
        return toBadRequestResponse(ex.getCode(), ex.getMessage());
    }

    private ResponseEntity<LimitErrorDto> toBadRequestResponse(String code, String message) {
        return new ResponseEntity<>(new LimitErrorDto(code, message), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<LimitErrorDto> toNotFoundResponse(String code, String message) {
        return new ResponseEntity<>(new LimitErrorDto(code, message), HttpStatus.NOT_FOUND);
    }
}
