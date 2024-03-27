package ru.alimov.limitservice.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.alimov.limitservice.demo.config.SettingsProperties;
import ru.alimov.limitservice.demo.exception.LimitServiceException;
import ru.alimov.limitservice.demo.exception.LockNotAcquiredException;
import ru.alimov.limitservice.demo.model.User;
import ru.alimov.limitservice.demo.repository.UserRepository;

import java.util.Objects;

import static ru.alimov.limitservice.demo.util.Const.DB_ERROR;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SettingsProperties settingsProperties;

    public UserServiceImpl(UserRepository userRepository,
                           SettingsProperties settingsProperties) {
        this.userRepository = userRepository;
        this.settingsProperties = settingsProperties;
    }

    @Override
    public Mono<Boolean> trySetLockByUserId(long userId) {
        return userRepository.trySetLockByUserId(userId)
                .switchIfEmpty(Mono.error(new LimitServiceException(DB_ERROR, "Locking user error")))
                .flatMap(r ->
                {
                    if (Objects.equals(r, 1L)) {
                        return Mono.just(Boolean.TRUE);
                    } else {
                        return Mono.error(new LockNotAcquiredException());
                    }
                });
    }

    @Override
    public Mono<Long> setUnLockByUserId(long userId) {
        return userRepository.setUnLockByUserId(userId)
                .switchIfEmpty(Mono.error(new LimitServiceException(DB_ERROR, "Unlocking user error")));
    }

    @Override
    public Mono<User> addNewUser(long userId) {
        return userRepository.insertUser(userId, false, settingsProperties.getDefaultMaxDailyUserLimit())
                .then(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new LimitServiceException(DB_ERROR, "Error creating user")));
    }

    @Override
    public Mono<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Flux<User> findAllUsers() {
        return userRepository.findAll();
    }

}
