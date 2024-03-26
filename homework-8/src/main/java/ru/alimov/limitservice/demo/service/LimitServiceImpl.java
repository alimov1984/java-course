package ru.alimov.limitservice.demo.service;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.alimov.limitservice.demo.config.SettingsProperties;
import ru.alimov.limitservice.demo.dto.LimitReleaseRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReleaseResponseDto;
import ru.alimov.limitservice.demo.dto.LimitReservationRequestDto;
import ru.alimov.limitservice.demo.dto.LimitReservationResponseDto;
import ru.alimov.limitservice.demo.exception.EntityNotFoundException;
import ru.alimov.limitservice.demo.exception.LimitServiceException;
import ru.alimov.limitservice.demo.exception.LimitValidationException;
import ru.alimov.limitservice.demo.exception.LockNotAcquiredException;
import ru.alimov.limitservice.demo.model.Limit;
import ru.alimov.limitservice.demo.repository.LimitRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static ru.alimov.limitservice.demo.util.Const.DB_ERROR;
import static ru.alimov.limitservice.demo.util.Const.EMPTY_FIELD_ERROR;
import static ru.alimov.limitservice.demo.util.Const.INCORRECT_FIELD_VALUE;
import static ru.alimov.limitservice.demo.util.Const.LIMIT_IS_EXCEEDED;
import static ru.alimov.limitservice.demo.util.Const.RELEASE_ALREADY_EXISTS;
import static ru.alimov.limitservice.demo.util.Const.RESERVATION_ALREADY_EXISTS;
import static ru.alimov.limitservice.demo.util.Const.RESERVATION_NOT_FOUND;
import static ru.alimov.limitservice.demo.util.Const.USER_LOCK_ACQUIRED_TIMEOUT;
import static ru.alimov.limitservice.demo.util.Const.USER_NOT_FOUND;

@Service
public class LimitServiceImpl implements LimitService {
    private static final long MAX_RETRY_ATTEMPTS = 10L;
    private static final long MAX_RETRY_DELAY = 1L;
    private final LimitRepository limitRepository;
    private final ConnectionFactory connectionFactory;
    private final UserService userService;
    private final SettingsProperties settingsProperties;
    private final LimitMapper limitMapper;

    public LimitServiceImpl(UserService userService,
                            LimitRepository limitRepository,
                            ConnectionFactory connectionFactory,
                            SettingsProperties settingsProperties,
                            LimitMapper limitMapper) {
        this.userService = userService;
        this.limitRepository = limitRepository;
        this.connectionFactory = connectionFactory;
        this.settingsProperties = settingsProperties;
        this.limitMapper = limitMapper;
    }

    @Override
    @Transactional
    public Mono<LimitReservationResponseDto> reserveLimit(Long userId, LimitReservationRequestDto requestDto) {
        return userService.findUserById(userId)
                .switchIfEmpty(userService.addNewUser(userId)
                        .flatMap(u -> addLimit(u.getId(),
                                UUID.randomUUID(),
                                OffsetDateTime.now(),
                                settingsProperties.getDefaultMaxDailyUserLimit())
                                .thenReturn(u)))
                .flatMap(u -> userService.trySetLockByUserId(userId))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(MAX_RETRY_DELAY))
                        .filter(throwable -> throwable instanceof LockNotAcquiredException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new LimitServiceException(USER_LOCK_ACQUIRED_TIMEOUT);
                        }))
                .flatMap(reserved -> checkAndReserveLimit(userId, requestDto.transactionId(), requestDto.sum()))
                .flatMap(r -> userService.setUnLockByUserId(userId).thenReturn(r))
                .onErrorResume(ex -> userService.setUnLockByUserId(userId).then(Mono.error(ex)));
    }

    @Override
    @Transactional
    public Mono<LimitReleaseResponseDto> releaseLimit(Long userId, LimitReleaseRequestDto requestDto) {
        return userService.findUserById(userId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(USER_NOT_FOUND, String.format("User with id=%s not found", userId))))
                .flatMap(u -> userService.trySetLockByUserId(userId))
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(MAX_RETRY_DELAY))
                        .filter(throwable -> throwable instanceof LockNotAcquiredException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new LimitServiceException(USER_LOCK_ACQUIRED_TIMEOUT);
                        }))
                .flatMap(reserved -> checkAndReleaseLimit(userId, requestDto.transactionId()))
                .flatMap(r -> userService.setUnLockByUserId(userId).thenReturn(r))
                .onErrorResume(ex -> userService.setUnLockByUserId(userId).then(Mono.error(ex)));
    }


    private Mono<LimitReservationResponseDto> checkAndReserveLimit(Long userId, UUID tranId, BigDecimal tranSum) {
        return limitRepository.findLimitDownByTransactionId(userId, tranId)
                .hasElement()
                .flatMap(reserveIsExist ->
                {
                    if (Boolean.TRUE.equals(reserveIsExist)) {
                        return Mono.error(new LimitServiceException(RESERVATION_ALREADY_EXISTS));
                    }
                    return limitRepository.getTodayLimitRest(userId)
                            .defaultIfEmpty(BigDecimal.ZERO)
                            .flatMap(limitSum ->
                            {
                                if (limitSum.subtract(tranSum).compareTo(BigDecimal.ZERO) < 0) {
                                    return Mono.just(limitMapper.toLimitReservationResponse(tranId, false, LIMIT_IS_EXCEEDED));
                                } else {
                                    return reserveLimit(userId, tranId, tranSum)
                                            .then(Mono.just(limitMapper.toLimitReservationResponse(tranId, true)));
                                }
                            });
                });
    }

    private Mono<LimitReleaseResponseDto> checkAndReleaseLimit(Long userId, UUID tranId) {
        return limitRepository.findLimitDownByTransactionId(userId, tranId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(RESERVATION_NOT_FOUND)))
                .flatMap(limit ->
                {
                    return limitRepository.findLimitUpByTransactionId(userId, tranId)
                            .hasElement()
                            .flatMap(releaseIsExist ->
                            {
                                if (Boolean.TRUE.equals(releaseIsExist)) {
                                    return Mono.error(new LimitServiceException(RELEASE_ALREADY_EXISTS));
                                }
                                return releaseLimit(userId, limit.getTransactionId(), limit.getTransactionDate(), limit.getSum())
                                        .then(Mono.just(limitMapper.toLimitReleaseResponse(tranId, true)));
                            });
                });
    }

    private Mono<Limit> reserveLimit(Long userId, UUID transactionId, BigDecimal sum) {
        return addLimit(userId, transactionId, OffsetDateTime.now(), BigDecimal.ZERO.subtract(sum.abs()))
                .flatMap(l -> Mono.just(l));
    }

    private Mono<Limit> releaseLimit(Long userId, UUID transactionId, OffsetDateTime transactionDate, BigDecimal sum) {
        return addLimit(userId, transactionId, transactionDate, sum.abs())
                .flatMap(l -> Mono.just(l));
    }


    private Mono<Limit> addLimit(Long userId, UUID transactionId, OffsetDateTime transactionDate, BigDecimal sum) {
        Limit limit = new Limit();
        limit.setUserId(userId);
        limit.setTransactionId(transactionId);
        limit.setRowDate(OffsetDateTime.now());
        limit.setTransactionDate(transactionDate);
        limit.setSum(sum);
        return limitRepository.save(limit)
                .switchIfEmpty(Mono.error(new LimitServiceException(DB_ERROR, "Adding limit error")));
    }

    public void validateReserveRequest(Long userId, LimitReservationRequestDto requestDto) {
        if (Objects.isNull(userId)) {
            throw new LimitValidationException(EMPTY_FIELD_ERROR, "User id has not be empty");
        }
        if (Objects.isNull(requestDto)) {
            throw new LimitValidationException(EMPTY_FIELD_ERROR, "Body has not be empty");
        }
        if (Objects.isNull(requestDto.transactionId())) {
            throw new LimitValidationException(EMPTY_FIELD_ERROR, "Field 'transactionId' has not be empty");
        }
        if (Objects.isNull(requestDto.sum())) {
            throw new LimitValidationException(EMPTY_FIELD_ERROR, "Field 'sum' has not be empty");
        }
        if (requestDto.sum().compareTo(BigDecimal.ZERO) <= 0) {
            throw new LimitValidationException(INCORRECT_FIELD_VALUE, "Field 'sum' has to be greater zero");
        }
    }

    public void validateReleaseRequest(Long userId, LimitReleaseRequestDto requestDto) {
        if (Objects.isNull(userId)) {
            throw new LimitValidationException(EMPTY_FIELD_ERROR, "User id has not be empty");
        }
        if (Objects.isNull(requestDto.transactionId())) {
            throw new LimitValidationException(EMPTY_FIELD_ERROR, "Field 'transactionId' has not be empty");
        }
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    @Override
    public Flux<Limit> setDailyLimitToUsers() {
        return userService.findAllUsers().flatMap(user -> addLimit(user.getId(),
                UUID.randomUUID(),
                OffsetDateTime.now(),
                user.getMaxDailyLimit()));
    }

}
