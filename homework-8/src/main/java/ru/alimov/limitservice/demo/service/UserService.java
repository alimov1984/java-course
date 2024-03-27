package ru.alimov.limitservice.demo.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.model.User;

public interface UserService {
    Mono<Boolean> trySetLockByUserId(long userId);

    Mono<Long> setUnLockByUserId(long userId);

    Mono<User> addNewUser(long userId);

    Mono<User> findUserById(long userId);

    Flux<User> findAllUsers();
}
