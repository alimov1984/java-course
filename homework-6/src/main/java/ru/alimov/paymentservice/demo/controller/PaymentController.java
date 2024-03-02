package ru.alimov.paymentservice.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alimov.paymentservice.demo.dto.PaymentDto;
import ru.alimov.paymentservice.demo.dto.ProductPaymentDto;
import ru.alimov.paymentservice.demo.exception.PaymentValidationException;
import ru.alimov.paymentservice.demo.exception.UserNotFoundException;
import ru.alimov.paymentservice.demo.integration.ProductService;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;
import ru.alimov.paymentservice.demo.service.MapperService;
import ru.alimov.paymentservice.demo.service.PaymentService;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final MapperService mapperService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final List<Long> userIdList = List.of(25L, 50L);

    public PaymentController(MapperService mapperService, PaymentService paymentService, ProductService productService) {
        this.mapperService = mapperService;
        this.paymentService = paymentService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductPaymentDto>> getProductByUserId(@RequestHeader(name = "USERID") Long userId) {
        if (Objects.isNull(userId)) {
            throw new UserNotFoundException("USERID header is not defined");
        }
        if (!userIdList.contains(userId)) {
            throw new UserNotFoundException(String.format("User with id=%d not found", userId));
        }

        List<ProductDto> productDtoList = productService.getProductsByUserId(userId);
        List<ProductPaymentDto> productPaymentDtoList =
                mapperService.convertListProductDtoToListProductPaymentDto(productDtoList);

        return new ResponseEntity<>(productPaymentDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Void> executePayment(@RequestHeader(name = "USERID") Long userId, @RequestBody PaymentDto paymentDto) {
        if (Objects.isNull(userId)) {
            throw new UserNotFoundException("USERID header is not defined");
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

        paymentService.executePayment(paymentDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
