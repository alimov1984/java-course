package ru.alimov.limitservice.demo.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.model.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE users SET is_locked = true WHERE id = $1 AND is_locked = false")
    Mono<Long> trySetLockByUserId(Long userId);

    @Modifying
    @Query("UPDATE users SET is_locked = false WHERE id = $1")
    Mono<Long> trySetUnLockByUserId(Long userId);

    @Modifying
    @Query("INSERT INTO users(id, is_locked) VALUES ($1, $2)")
    Mono<Long> insertUser(Long userId, Boolean isLocked);

}
