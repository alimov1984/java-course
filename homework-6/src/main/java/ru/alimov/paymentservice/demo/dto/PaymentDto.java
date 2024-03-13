package ru.alimov.paymentservice.demo.dto;

import java.math.BigDecimal;

public record PaymentDto(Long debetProductId, Long creditProductId, BigDecimal sum) {
}
