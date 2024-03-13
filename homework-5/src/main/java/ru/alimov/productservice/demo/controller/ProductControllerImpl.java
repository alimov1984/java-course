package ru.alimov.productservice.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alimov.productservice.demo.dto.ErrorDto;
import ru.alimov.productservice.demo.dto.ProductDto;
import ru.alimov.productservice.demo.model.ProductType;
import ru.alimov.productservice.demo.service.ProductService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/products")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";

    public ProductControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/by-product-id/{id}")
    @Override
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long productId) {
        if (Objects.isNull(productId)) {
            return prepareErrorResponse(PRODUCT_NOT_FOUND, String.format("Product not found"));
        }

        ProductDto productDto = productService.getProductById(productId);
        if (productDto == null) {
            return prepareErrorResponse(PRODUCT_NOT_FOUND, String.format("Product not found"));
        }
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/by-user-id/{id}")
    @Override
    public ResponseEntity<?> getProductByUserId(@PathVariable(name = "id") Long userId) {
        if (Objects.isNull(userId)) {
            return prepareErrorResponse(PRODUCT_NOT_FOUND, String.format("Products are not found"));
        }

        List<ProductDto> productDtoList = productService.getProductByUserId(userId);

        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    private ResponseEntity<ErrorDto> prepareErrorResponse(String code, String message) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(code);
        errorDto.setMessage(message);
        ResponseEntity<ErrorDto> responseEntity = new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }
}
