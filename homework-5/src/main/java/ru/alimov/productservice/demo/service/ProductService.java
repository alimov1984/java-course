package ru.alimov.productservice.demo.service;

import ru.alimov.productservice.demo.dto.ProductDto;

import java.util.List;

public interface ProductService {
    void addProduct(Long userId, ProductDto product);

    void updateProduct(Long userId, ProductDto product);

    void deleteProduct(Long productId);

    void deleteAllProducts();

    ProductDto getProductById(Long id);

    List<ProductDto> getProductByUserId(Long userId);
}
