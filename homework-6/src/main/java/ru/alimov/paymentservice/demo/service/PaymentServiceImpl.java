package ru.alimov.paymentservice.demo.service;

import org.springframework.stereotype.Service;
import ru.alimov.paymentservice.demo.dto.PaymentDto;
import ru.alimov.paymentservice.demo.exception.EntityNotFoundException;
import ru.alimov.paymentservice.demo.exception.PaymentValidationException;
import ru.alimov.paymentservice.demo.exception.ProductInsufficientFundsException;
import ru.alimov.paymentservice.demo.integration.ProductService;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;

import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private final String PRODUCT_NOT_FOUND_CODE = "PRODUCT_NOT_FOUND";
    private final ProductService productService;

    public PaymentServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void executePayment(Long userId, PaymentDto paymentDto) {
        if (Objects.isNull(userId)) {
            throw new EntityNotFoundException(USER_NOT_FOUND_CODE, "USERID header is not defined");
        }
        if (Objects.isNull(paymentDto.debetProductId())) {
            throw new PaymentValidationException("Field debetProductId have to be filled");
        }
        if (Objects.isNull(paymentDto.creditProductId())) {
            throw new PaymentValidationException("Field creditProductId have to be filled");
        }
        if (Objects.isNull(paymentDto.sum())) {
            throw new PaymentValidationException("Field sum have to be filled");
        }

        ProductDto debetProduct = productService.getProductById(paymentDto.debetProductId());
        if (debetProduct == null) {
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND_CODE, String.format("Product with id=%d not found", paymentDto.debetProductId()));
        }

        ProductDto creditProduct = productService.getProductById(paymentDto.creditProductId());
        if (creditProduct == null) {
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND_CODE, String.format("Product with id=%d not found", paymentDto.creditProductId()));
        }

        if (debetProduct.getBalance().compareTo(paymentDto.sum()) <= 0) {
            throw new ProductInsufficientFundsException(
                    String.format("Product with id=%d doesn't have enough funds on the balance", paymentDto.debetProductId()));
        }

        //Processing payment
    }

}
