package ru.alimov.limitservice.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.dto.ErrorDto;
import ru.alimov.limitservice.demo.dto.LimitReservationRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;
import ru.alimov.limitservice.demo.service.LimitService;

@RestController
@RequestMapping("/api/v1/limit")
public class LimitController {
    private final LimitService limitService;

    public LimitController(LimitService limitService)
    {
        this.limitService = limitService;
    }

    @PostMapping("/reserve")
    public Mono<ResponseEntity<LimitReservationResponseDto>> reserve(
            @RequestHeader(name = "USERID") Long userId,
            @RequestBody LimitReservationRequestDto requestDto) {

        return limitService.reserve(userId, requestDto)
                .flatMap(r -> Mono.just(new ResponseEntity<>(r, HttpStatus.OK)));
    }

    private ResponseEntity<ErrorDto> prepareErrorResponse(String code, String message) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(code);
        errorDto.setMessage(message);
        ResponseEntity<ErrorDto> responseEntity = new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

}
