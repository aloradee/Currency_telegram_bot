package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.impl.CryptoCurrencyServiceImpl;
import com.skillbox.cryptobot.service.impl.SubscribeServiceImpl;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import static com.skillbox.cryptobot.utils.TelegramMessageSender.sendMessage;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {

    private final SubscribeServiceImpl subscribeService;
    private final CryptoCurrencyServiceImpl cryptoCurrencyService;


    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        long telegramId = message.getFrom().getId();
        answer.setChatId(message.getChatId());

        if (arguments == null || arguments.length == 0 || arguments[0].trim().isEmpty()) {
            answer.setText("Пожалуйста, укажите цену для подписки. Пример: /subscribe 50000");
            sendMessage(answer, absSender);
            return;
        }

        try {
            double subscriptionPrice = Double.parseDouble(arguments[0]);

            if (subscriptionPrice <= 0) {
                answer.setText("Цена подписки должна быть положительным числом");
                sendMessage(answer, absSender);
                return;
            }

            answer.setText("Текущая цена биткоина " + TextUtil.toString(cryptoCurrencyService.getBitcoinPrice()) + " USD");
            sendMessage(answer, absSender);

            subscribeService.createOrUpdateSubscription(telegramId, subscriptionPrice);

            String response = String.format("Новая подписка создана на стоимость: %.2f USD", subscriptionPrice);
            answer.setText(response);

        } catch (NumberFormatException e) {
            answer.setText("Неверный формат цены. Используйте числа. Пример: /subscribe 50000 или /subscribe 45000.50");
        } catch (IOException e) {
            log.error("Ошибка при получении цены биткоина", e);
            answer.setText("Не удалось получить текущую цену биткоина. Подписка создана, но текущая цена недоступна.");

            double subscriptionPrice = Double.parseDouble(arguments[0]);
            subscribeService.createOrUpdateSubscription(telegramId, subscriptionPrice);
        }

        sendMessage(answer, absSender);

    }

}