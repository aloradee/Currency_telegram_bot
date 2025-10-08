package com.skillbox.cryptobot.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "subscribers")
@Entity
@Data
@NoArgsConstructor
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, name = "telegram_id", unique = true)
    private Long telegramId;

    @Column(name = "subscribe_price")
    private Double subscribePrice;

    public Subscriber(Long telegramId) {
        this.telegramId = telegramId;
        this.subscribePrice = null;
    }
}
