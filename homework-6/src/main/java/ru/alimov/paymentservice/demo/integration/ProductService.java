package ru.alimov.paymentservice.demo.integration;

import ru.alimov.paymentservice.demo.integration.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getProductsByUserId(Long userId);

    ProductDto getProductById(Long productId);
}
