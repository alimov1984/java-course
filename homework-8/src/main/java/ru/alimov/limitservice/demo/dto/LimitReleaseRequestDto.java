package ru.alimov.limitservice.demo.dto;

import java.util.UUID;

public record LimitReleaseRequestDto(UUID transactionId) {
}
