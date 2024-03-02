package ru.alimov.paymentservice.demo.service;

import org.springframework.stereotype.Service;
import ru.alimov.paymentservice.demo.dto.ProductPaymentDto;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperServiceImpl implements MapperService {
    @Override
    public ProductPaymentDto convertProductDtoToProductPaymentDto(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        ProductPaymentDto productPaymentDto = new ProductPaymentDto();
        productPaymentDto.setId(productDto.getId());
        productPaymentDto.setType(productDto.getType());
        productPaymentDto.setAccountNumber(productDto.getAccountNumber());
        productPaymentDto.setBalance(productDto.getBalance());
        return productPaymentDto;
    }

    @Override
    public List<ProductPaymentDto> convertListProductDtoToListProductPaymentDto(List<ProductDto> productDtoList) {
        List<ProductPaymentDto> productPaymentDtoList = new ArrayList<>();
        if (productDtoList == null) {
            return productPaymentDtoList;
        }
        productPaymentDtoList = productDtoList.stream()
                .map(this::convertProductDtoToProductPaymentDto)
                .collect(Collectors.toList());

        return productPaymentDtoList;
    }
}
