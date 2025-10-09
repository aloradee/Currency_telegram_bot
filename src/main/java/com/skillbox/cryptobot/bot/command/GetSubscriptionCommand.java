package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.dto.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscriberRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long telegramId = message.getFrom().getId();
        Optional<Subscriber> existingSubscriber = repository.findByTelegramId(telegramId);
        SendMessage answer = new SendMessage();
        Subscriber subscriber;
        if(existingSubscriber.isPresent()) {
            subscriber = existingSubscriber.get();
            if(subscriber.getSubscribePrice() != null) {
                answer.setText("Вы подписаны на стоимость биткоина " + subscriber.getSubscribePrice() + "USD");
                sendMessage(answer, absSender);
            } else {
                answer.setText("Активные подписки отсутствуют");
                sendMessage(answer, absSender);
            }
        } else {
            log.error("Пользователь с telegramId {} не найден", telegramId);
        }


    }

    private void sendMessage(SendMessage answer, AbsSender absSender) {
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }
}