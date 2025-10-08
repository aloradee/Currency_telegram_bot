package com.skillbox.cryptobot.service.impl;

import com.skillbox.cryptobot.dto.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.SubscribeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscriberRepository subscribeRepository;

    @Override
    @Transactional
    public void createUserSubscription(Long telegramId) {
        if(subscribeRepository.existsByTelegramId(telegramId)) {
            log.info("Подписчик с telegramId {} уже существует.", telegramId);
            return;
        }
        Subscriber newSubscriber = new Subscriber(telegramId);
        subscribeRepository.save(newSubscriber);
        log.info("Новый подписчик создан с telegramId {}", telegramId);
    }

}
