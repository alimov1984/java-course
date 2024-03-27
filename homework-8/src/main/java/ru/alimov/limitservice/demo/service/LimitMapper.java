package ru.alimov.limitservice.demo.service;

import ru.alimov.limitservice.demo.dto.LimitReleaseResponseDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;

import java.util.UUID;

public interface LimitMapper {
    LimitReservationResponseDto toLimitReservationResponse(UUID transactionId, boolean isSuccess, String reasonCode);
    LimitReservationResponseDto toLimitReservationResponse(UUID transactionId, boolean isSuccess);
    LimitReleaseResponseDto toLimitReleaseResponse(UUID transactionId, boolean isSuccess, String reasonCode);
    LimitReleaseResponseDto toLimitReleaseResponse(UUID transactionId, boolean isSuccess);
}
