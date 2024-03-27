package ru.alimov.limitservice.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.dto.LimitReleaseRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReleaseResponseDto;
import ru.alimov.limitservice.demo.dto.LimitReservationRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;
import ru.alimov.limitservice.demo.service.LimitService;

@RestController
@RequestMapping("/api/v1/limit")
public class LimitController {
    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @PostMapping("/reserve")
    public Mono<ResponseEntity<LimitReservationResponseDto>> reserveLimit(
            @RequestHeader(name = "USERID") Long userId,
            @RequestBody LimitReservationRequestDto requestDto) {

        limitService.validateReserveRequest(userId, requestDto);

        return limitService.reserveLimit(userId, requestDto)
                .flatMap(r -> Mono.just(new ResponseEntity<>(r, HttpStatus.OK)));
    }

    @PostMapping("/release")
    public Mono<ResponseEntity<LimitReleaseResponseDto>> releaseLimit(
            @RequestHeader(name = "USERID") Long userId,
            @RequestBody LimitReleaseRequestDto requestDto) {

        limitService.validateReleaseRequest(userId, requestDto);

        return limitService.releaseLimit(userId, requestDto)
                .flatMap(r -> Mono.just(new ResponseEntity<>(r, HttpStatus.OK)));
    }

}
