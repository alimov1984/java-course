package ru.alimov.limitservice.demo.dto;

import java.util.UUID;

public record LimitReleaseResponseDto(UUID transactionId, Boolean limitReleaseIsSuccess, String reasonCode) {
}
