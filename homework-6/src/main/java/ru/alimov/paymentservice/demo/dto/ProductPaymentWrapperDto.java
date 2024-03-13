package ru.alimov.paymentservice.demo.dto;

import java.util.List;

public class ProductPaymentWrapperDto {
    private List<ProductPaymentDto> productPaymentDtoList;

    public void setProductPaymentDtoList(List<ProductPaymentDto> productPaymentDtoList) {
        this.productPaymentDtoList = productPaymentDtoList;
    }

    public List<ProductPaymentDto> getProductPaymentDtoList() {
        return this.productPaymentDtoList;
    }


}
