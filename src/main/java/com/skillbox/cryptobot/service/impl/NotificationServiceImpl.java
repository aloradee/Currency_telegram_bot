package com.skillbox.cryptobot.service.impl;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.config.NotificationConfig;
import com.skillbox.cryptobot.dto.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.CryprtoCurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl {

    private final CryptoBot cryptoBot;
    private final CryprtoCurrencyService cryprtoCurrencyService;
    private final SubscriberRepository subscriberRepository;
    private final NotificationConfig notificationConfig;

    private final Map<Long, LocalDateTime> lastNotificationTime = new HashMap<>();

    @Scheduled(fixedDelayString = "${app.notification.price-check-interval:120000}")
    public void checkPriceAndNotify() {
        try {
            Double currentPrice = cryprtoCurrencyService.getBitcoinPrice();
            if(currentPrice == null) {
                log.warn("Не удалось получить текущую цену биткоина");
                return;
            }

            log.debug("Текущая цена биткоина: {}", currentPrice);

            List<Subscriber> subscribersToNotify = subscriberRepository.findBySubscriptionPriceGreaterThan(currentPrice);
            for(Subscriber subscriber : subscribersToNotify) {
                if(shouldSendNotification(subscriber.getTelegramId())) {
                    sendNotification(subscriber.getTelegramId(), currentPrice);
                    updateLastNotificationTime(subscriber.getTelegramId());
                }
            }
        } catch (IOException e) {
            log.error("Ошибка при получении цены и отправке уведомлений", e);
        }
    }

    private boolean shouldSendNotification(Long telegramId) {
        LocalDateTime lastTime = lastNotificationTime.get(telegramId);
        if(lastTime == null) {
            return true;
        }

        LocalDateTime nextAllowedTime = lastTime.plusMinutes(
                notificationConfig.getMinNotificationInterval()
        );
        return LocalDateTime.now().isAfter(nextAllowedTime);
    }

    private void sendNotification(Long telegramId, Double currentPrice) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(telegramId.toString());
            message.setText(String.format("Пора выкупать, стоимость биткоина %.2f USD", currentPrice));

            cryptoBot.execute(message);
            log.info("Уведомление отправлено пользователю: {}, цена: {}", telegramId, currentPrice);

        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке уведомления пользователю {}", telegramId, e);
        }
    }

    private void updateLastNotificationTime(Long telegramId) {
        lastNotificationTime.put(telegramId, LocalDateTime.now());
    }

    @Scheduled(fixedRate = 360000)
    public void cleanupOldNotifications() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        lastNotificationTime.entrySet().removeIf(entry -> entry
                .getValue()
                .isBefore(cutoffTime));
    }

}
