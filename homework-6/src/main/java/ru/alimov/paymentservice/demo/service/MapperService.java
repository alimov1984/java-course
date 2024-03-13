package ru.alimov.paymentservice.demo.service;

import ru.alimov.paymentservice.demo.dto.ProductPaymentDto;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;

import java.util.List;

public interface MapperService {
    ProductPaymentDto convertProductDtoToProductPaymentDto(ProductDto productDto);

    List<ProductPaymentDto> convertListProductDtoToListProductPaymentDto(List<ProductDto> productDto);
}
