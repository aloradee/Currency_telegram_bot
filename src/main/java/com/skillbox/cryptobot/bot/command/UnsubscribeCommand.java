package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.dto.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.impl.SubscribeServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final SubscribeServiceImpl subscribeService;
    private final SubscriberRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long telegramId = message.getFrom().getId();
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId().toString());

        try {
            subscribeService.unsubscribeSubscription(telegramId);
            answer.setText("Подписка отменена");
        } catch (Exception e) {
            answer.setText("Активные подписки отсутствуют");
            log.warn("Попытка отмены несуществующей подписки для пользователя {}", telegramId);
        }
        sendMessage(answer, absSender);
    }
    private void sendMessage(SendMessage answer, AbsSender absSender) {
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения пользователю {}", answer.getChatId(), e);
        }
    }
}