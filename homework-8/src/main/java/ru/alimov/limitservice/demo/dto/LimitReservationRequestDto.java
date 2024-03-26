package ru.alimov.limitservice.demo.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record LimitReservationRequestDto(UUID transactionId,BigDecimal sum) {
}
