package ru.alimov.limitservice.demo.dto;

import java.util.UUID;

public record LimitReservationResponseDto(UUID transactionId, Boolean limitReservationIsSuccess, String reasonCode) {
}
