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

    @PostConstruct
    public void initDb() {
        productService.deleteAllProducts();

        List<ProductDto> productDtoList = new ArrayList<>();

        ProductDto productDto1 = new ProductDto();
        productDto1.setAccountNumber("12345");
        productDto1.setBalance(new BigDecimal(525000.2040));
        productDto1.setType(ProductType.ACCOUNT);
        productDtoList.add(productDto1);

        ProductDto productDto2 = new ProductDto();
        productDto2.setAccountNumber("5680954");
        productDto2.setBalance(new BigDecimal(25000.430));
        productDto2.setType(ProductType.ACCOUNT);
        productDtoList.add(productDto2);

        ProductDto productDto3 = new ProductDto();
        productDto3.setAccountNumber("8766432");
        productDto3.setBalance(new BigDecimal(1895));
        productDto3.setType(ProductType.CARD);
        productDtoList.add(productDto3);

        for (ProductDto productDto : productDtoList) {
            productService.addProduct(25L, productDto);
        }

        List<ProductDto> productDtoList2 = new ArrayList<>();

        ProductDto productDto4 = new ProductDto();
        productDto4.setAccountNumber("32437");
        productDto4.setBalance(new BigDecimal(12667.6542));
        productDto4.setType(ProductType.ACCOUNT);
        productDtoList2.add(productDto4);

        ProductDto productDto5 = new ProductDto();
        productDto5.setAccountNumber("83554");
        productDto5.setBalance(new BigDecimal(22344.5644));
        productDto5.setType(ProductType.CARD);
        productDtoList2.add(productDto5);

        for (ProductDto productDto : productDtoList2) {
            productService.addProduct(50L, productDto);
        }

    }

    @GetMapping("/by-product-id/{id}")
    @Override
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long productId) {
        if (Objects.isNull(productId)) {
            return prepareErrorResponse(PRODUCT_NOT_FOUND, String.format("Product not found"));
        }

        ProductDto productDto = productService.getProductById(productId);

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
