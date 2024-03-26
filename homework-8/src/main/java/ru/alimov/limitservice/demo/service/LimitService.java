package ru.alimov.limitservice.demo.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.dto.LimitReleaseRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReleaseResponseDto;
import ru.alimov.limitservice.demo.dto.LimitReservationRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;
import ru.alimov.limitservice.demo.model.Limit;

public interface LimitService {
    Mono<LimitReservationResponseDto> reserveLimit(Long userId, LimitReservationRequestDto requestDto);

    Mono<LimitReleaseResponseDto> releaseLimit(Long userId, LimitReleaseRequestDto requestDto);

    void validateReserveRequest(Long userId, LimitReservationRequestDto requestDto);
    void validateReleaseRequest(Long userId, LimitReleaseRequestDto requestDto);

    Flux<Limit> setDailyLimitToUsers();

}
