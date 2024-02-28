package ru.alimov.productservice.demo.controller;

import org.springframework.http.ResponseEntity;


public interface ProductController {
    ResponseEntity<?> getProductById(Long productId);

    ResponseEntity<?> getProductByUserId(Long userId);
}
