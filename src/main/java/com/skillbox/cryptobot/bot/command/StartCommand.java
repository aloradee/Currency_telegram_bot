package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.impl.SubscribeServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.skillbox.cryptobot.utils.TelegramMessageSender.sendMessage;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {

    private final SubscribeServiceImpl subscribeService;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /subscribe [число] - подписаться на стоимость биткоина в USD
                 /get_price - получить стоимость биткоина
                 /get_subscription - получить текущую подписку
                 /unsubscribe - отменить подписку на стоимость
                """);
        User user = message.getFrom();
        Long userId = user.getId();

        subscribeService.createUserSubscription(userId);

        sendMessage(answer, absSender);
    }

}