package ru.alimov.limitservice.demo.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.model.Limit;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface LimitRepository extends ReactiveCrudRepository<Limit, Long> {

    @Query("SELECT SUM(sum) FROM limits WHERE user_id = $1 AND DATE(CAST(transaction_date AS VARCHAR)) = DATE(CURRENT_TIMESTAMP)")
    Mono<BigDecimal> getTodayLimitRest(Long userId);

    @Query("SELECT id, row_date, transaction_id, transaction_date, user_id, sum FROM limits WHERE user_id = $1 AND transaction_id = $2 AND sum < 0 LIMIT 1")
    Mono<Limit> findLimitDownByTransactionId(long userId, UUID transaction_id);

    @Query("SELECT id, row_date, transaction_id, transaction_date, user_id, sum FROM limits WHERE user_id = $1 AND transaction_id = $2 AND sum > 0 LIMIT 1")
    Mono<Limit> findLimitUpByTransactionId(long userId, UUID transaction_id);

    @Modifying
    @Query("INSERT INTO limits(row_date, transaction_id, transaction_date, user_id, sum) SELECT CURRENT_TIMESTAMP, gen_random_uuid(), CURRENT_TIMESTAMP, id, $1 FROM users")
    Mono<Long> insertDailyLimit(BigDecimal limitSum);

}
