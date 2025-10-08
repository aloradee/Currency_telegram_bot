package com.skillbox.cryptobot.repositories;

import com.skillbox.cryptobot.dto.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Optional<Subscriber> findByTelegramId(Long telegramId);
    boolean existsByTelegramId(Long telegramId);
}
