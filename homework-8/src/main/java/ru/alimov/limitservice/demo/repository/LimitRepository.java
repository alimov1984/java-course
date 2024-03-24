package ru.alimov.limitservice.demo.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.model.Limit;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Repository
public interface LimitRepository extends ReactiveCrudRepository<Limit, Long> {
    @Query("SELECT SUM(sum) FROM limits WHERE user_id = $1 AND DATE(CAST(transaction_date AS VARCHAR)) = DATE(CURRENT_TIMESTAMP)")
    Mono<BigDecimal> getTodayRestLimit(Long userId);

//    @Modifying
//    @Query("INSERT INTO limits(row_date, transaction_id, transaction_date, user_id, sum) VALUES ($1, $2, $3, $4, $5)")
//    Mono<Long> insertLimit(Timestamp rowDate, UUID transactionId, Timestamp transactionDate, Long userId, BigDecimal sum);

}
