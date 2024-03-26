package ru.alimov.limitservice.demo.service;

import org.springframework.stereotype.Service;
import ru.alimov.limitservice.demo.dto.LimitReleaseResponseDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;

import java.util.UUID;

@Service
public class LimitMapperImpl implements LimitMapper {
    @Override
    public LimitReservationResponseDto toLimitReservationResponse(UUID transactionId, boolean isSuccess, String reasonCode) {
        LimitReservationResponseDto responseDto = new LimitReservationResponseDto(transactionId, isSuccess, reasonCode);
        return responseDto;
    }

    @Override
    public LimitReservationResponseDto toLimitReservationResponse(UUID transactionId, boolean isSuccess) {
        LimitReservationResponseDto responseDto = new LimitReservationResponseDto(transactionId, isSuccess, null);
        return responseDto;
    }

    @Override
    public LimitReleaseResponseDto toLimitReleaseResponse(UUID transactionId, boolean isSuccess, String reasonCode) {
        LimitReleaseResponseDto responseDto = new LimitReleaseResponseDto(transactionId, isSuccess, reasonCode);
        return responseDto;
    }

    @Override
    public LimitReleaseResponseDto toLimitReleaseResponse(UUID transactionId, boolean isSuccess) {
        LimitReleaseResponseDto responseDto = new LimitReleaseResponseDto(transactionId, isSuccess, null);
        return responseDto;
    }
}
