package ru.alimov.limitservice.demo.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.model.User;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE users SET is_locked = true WHERE id = $1 AND is_locked = false")
    Mono<Long> trySetLockByUserId(long userId);

    @Modifying
    @Query("UPDATE users SET is_locked = false WHERE id = $1")
    Mono<Long> setUnLockByUserId(long userId);

    @Modifying
    @Query("INSERT INTO users(id, is_locked, max_daily_limit) VALUES ($1, $2, $3)")
    Mono<Long> insertUser(long userId, Boolean isLocked, BigDecimal maxDailyLimit);

}
