package ru.alimov.paymentservice.demo.service;

import org.springframework.stereotype.Service;
import ru.alimov.paymentservice.demo.dto.PaymentDto;
import ru.alimov.paymentservice.demo.exception.ProductInsufficientFundsException;
import ru.alimov.paymentservice.demo.exception.ProductNotFoundException;
import ru.alimov.paymentservice.demo.integration.ProductService;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final ProductService productService;

    public PaymentServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void executePayment(PaymentDto paymentDto) {
        ProductDto debetProduct = productService.getProductById(paymentDto.debetProductId());
        if (debetProduct == null) {
            throw new ProductNotFoundException(String.format("Product with id=%d not found", paymentDto.debetProductId()));
        }

        ProductDto creditProduct = productService.getProductById(paymentDto.creditProductId());
        if (creditProduct == null) {
            throw new ProductNotFoundException(String.format("Product with id=%d not found", paymentDto.creditProductId()));
        }

        if (debetProduct.getBalance().compareTo(paymentDto.sum()) <= 0) {
            throw new ProductInsufficientFundsException(
                    String.format("Product with id=%d doesn't have enough funds on the balance", paymentDto.debetProductId()));
        }

        //Processing payment
    }

}
