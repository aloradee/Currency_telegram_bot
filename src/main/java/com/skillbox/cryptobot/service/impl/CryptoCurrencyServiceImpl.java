package com.skillbox.cryptobot.service.impl;

import com.skillbox.cryptobot.client.BinanceClient;
import com.skillbox.cryptobot.service.CryprtoCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class CryptoCurrencyServiceImpl implements CryprtoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;


    public CryptoCurrencyServiceImpl(BinanceClient client) {
        this.client = client;
    }

    @Override
    public double getBitcoinPrice() throws IOException {
        try {
            double currentPrice = client.getBitcoinPrice();
            price.set(currentPrice);
            return currentPrice;
        } catch (IOException e) {
            Double cachedPrice = price.get();
            if(cachedPrice != null) {
                log.warn("Не удалось получить актуальную цену, возвращаем кешированную");
                return cachedPrice;
            }
            throw e;
        }
    }
}
