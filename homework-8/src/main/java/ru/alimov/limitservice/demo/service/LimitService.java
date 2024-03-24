package ru.alimov.limitservice.demo.service;

import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.dto.LimitReservationRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;

public interface LimitService {
    Mono<LimitReservationResponseDto> reserve(Long userId, LimitReservationRequestDto requestDto);
}
