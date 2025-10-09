package com.skillbox.cryptobot.service;

public interface SubscribeService {
    void createOrUpdateSubscription(Long telegramId, Double priceSubscription);
    void createUserSubscription(Long telegramId);

}
