package com.skillbox.cryptobot.repositories;

import com.skillbox.cryptobot.dto.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Optional<Subscriber> findByTelegramId(Long telegramId);
    boolean existsByTelegramId(Long telegramId);
    @Query("SELECT s FROM Subscriber s WHERE s.subscriptionPrice > :currentPrice AND s.subscriptionPrice IS NOT NULL")
    List<Subscriber> findBySubscriptionPriceGreaterThan(@Param("currentPrice") Double currentPrice);
}
