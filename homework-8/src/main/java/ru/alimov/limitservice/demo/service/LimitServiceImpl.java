package ru.alimov.limitservice.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.dto.LimitReservationRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;
import ru.alimov.limitservice.demo.model.Limit;
import ru.alimov.limitservice.demo.model.User;
import ru.alimov.limitservice.demo.repository.LimitRepository;
import ru.alimov.limitservice.demo.repository.UserRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static ru.alimov.limitservice.demo.Const.INIT_USER_LIMIT;

@Service
public class LimitServiceImpl implements LimitService {
    private final UserRepository userRepository;
    private final LimitRepository limitRepository;

    Logger logger = LoggerFactory.getLogger(LimitServiceImpl.class);

    public LimitServiceImpl(UserRepository userRepository, LimitRepository limitRepository) {
        this.userRepository = userRepository;
        this.limitRepository = limitRepository;
    }

    @Override
    @Transactional
    public Mono<LimitReservationResponseDto> reserve(Long userId, LimitReservationRequestDto requestDto) {
        Mono<LimitReservationResponseDto> result = userRepository.findById(userId)
                .switchIfEmpty(addNewUser(userId))
                .flatMap(u -> userRepository.trySetLockByUserId(userId))
                .flatMap(r ->
                {
                    if (r > 0) {
                        return checkAndChangeLimit(userId, requestDto);
                    } else {
                        return prepareLimitResponse(requestDto.getTransactionId(), false);
                    }
                })
                .flatMap(r -> userRepository.trySetUnLockByUserId(userId).thenReturn(r))
                .doOnError(e -> userRepository.trySetUnLockByUserId(userId));
        return result;
    }

    private Mono<User> addNewUser(Long userId) {
        return createUser(userId).
                flatMap(u ->
                {
                    return addLimit(u.getId(), UUID.randomUUID(), INIT_USER_LIMIT)
                            .flatMap(l -> Mono.just(u));
                });
    }

    private Mono<LimitReservationResponseDto> checkAndChangeLimit(Long userId, LimitReservationRequestDto requestDto) {
        return limitRepository.getTodayRestLimit(userId).flatMap(lsum ->
        {
            if (lsum.subtract(requestDto.getSum()).compareTo(BigDecimal.ZERO) <= 0) {
                return prepareLimitResponse(requestDto.getTransactionId(), false);
            }
            return changeLimit(userId, requestDto);
        });
    }

    private Mono<LimitReservationResponseDto> changeLimit(Long userId, LimitReservationRequestDto requestDto) {
        return addLimit(userId, requestDto.getTransactionId(), BigDecimal.ZERO.subtract(requestDto.getSum()))
                .flatMap(l -> {
                    return prepareLimitResponse(requestDto.getTransactionId(), l != null ? true : false);
                });
    }

    private Mono<User> createUser(Long userId) {
        return userRepository.insertUser(userId, false).flatMap(u -> userRepository.findById(userId));
    }

    private Mono<Limit> addLimit(Long userId, UUID transactionId, BigDecimal sum) {
        Limit limit = new Limit();
        limit.setUserId(userId);
        limit.setTransactionId(transactionId);
        limit.setRowDate(Timestamp.from(Instant.now()));
        limit.setTransactionDate(Timestamp.from(Instant.now()));
        limit.setSum(sum);
        return limitRepository.save(limit);
//        return limitRepository.insertLimit(
//                        limit.getRowDate(),
//                        limit.getTransactionId(),
//                        limit.getTransactionDate(),
//                        limit.getUserId(),
//                        limit.getSum())
//                .flatMap(l -> limitRepository.findById(l));
    }

    private Mono<LimitReservationResponseDto> prepareLimitResponse(UUID transactionId, boolean isSuccess) {
        LimitReservationResponseDto responseDto = new LimitReservationResponseDto();
        responseDto.setTransactionId(transactionId);
        responseDto.setLimitReservationIsSuccess(isSuccess);
        return Mono.just(responseDto);
    }
}
