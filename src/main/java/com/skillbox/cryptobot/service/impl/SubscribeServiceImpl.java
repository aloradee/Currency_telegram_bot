package com.skillbox.cryptobot.service.impl;

import com.skillbox.cryptobot.dto.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.SubscribeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscriberRepository subscribeRepository;

    @Override
    public void createOrUpdateSubscription(Long telegramId, Double priceSubscription) {

        Optional<Subscriber> existingSubscriber = subscribeRepository.findByTelegramId(telegramId);

        Subscriber subscriber;

        if(existingSubscriber.isPresent()) {
            subscriber = existingSubscriber.get();
            subscriber.setSubscribePrice(priceSubscription);
            log.info("Обновлена подписка для пользователя с telegramId {}, на цену {}", telegramId, priceSubscription);
        } else {
            subscriber = new Subscriber(telegramId, priceSubscription);
            log.info("Создана новая подписка для пользователя с telegramId {}, на цену {}", telegramId,
                    priceSubscription);
        }

        subscribeRepository.save(subscriber);
    }

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
