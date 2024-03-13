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
import ru.alimov.paymentservice.demo.dto.ProductPaymentWrapperDto;
import ru.alimov.paymentservice.demo.integration.ProductService;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;
import ru.alimov.paymentservice.demo.service.MapperService;
import ru.alimov.paymentservice.demo.service.PaymentService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final MapperService mapperService;
    private final PaymentService paymentService;
    private final ProductService productService;

    public PaymentController(MapperService mapperService, PaymentService paymentService, ProductService productService) {
        this.mapperService = mapperService;
        this.paymentService = paymentService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<ProductPaymentWrapperDto> getProductByUserId(@RequestHeader(name = "USERID") Long userId) {
        List<ProductDto> productDtoList = productService.getProductsByUserId(userId);
        List<ProductPaymentDto> productPaymentDtoList =
                mapperService.convertListProductDtoToListProductPaymentDto(productDtoList);

        ProductPaymentWrapperDto productPaymentWrapperDto = new ProductPaymentWrapperDto();
        productPaymentWrapperDto.setProductPaymentDtoList(productPaymentDtoList);

        return new ResponseEntity<>(productPaymentWrapperDto, HttpStatus.OK);
    }

    @PostMapping()
    public void executePayment(@RequestHeader(name = "USERID") Long userId, @RequestBody PaymentDto paymentDto) {
        paymentService.executePayment(userId, paymentDto);
    }

}
