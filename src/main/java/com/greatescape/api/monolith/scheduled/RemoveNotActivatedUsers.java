package com.greatescape.api.monolith.scheduled;

import com.greatescape.api.monolith.domain.User;
import com.greatescape.api.monolith.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveNotActivatedUsers implements Runnable {

    private final UserRepository userRepository;

    private final CacheManager cacheManager;

    /**
     * Not activated users should be automatically deleted after 3 days.
     */
    @Scheduled(cron = "${app.cron.remove-not-activated-users}")
    @Override
    public void run() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(
                Instant.now().minus(3, ChronoUnit.DAYS)
            ).forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)
        ).evict(user.getLogin());

        if (user.getEmail() != null) {
            Objects.requireNonNull(
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)
            ).evict(user.getEmail());
        }
    }
}
