package com.skillbox.cryptobot.service.impl;

import com.skillbox.cryptobot.client.BinanceClient;
import com.skillbox.cryptobot.dto.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.CryprtoCurrencyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class CryptoCurrencyServiceImpl implements CryprtoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;
    private final SubscriberRepository repository;


    public CryptoCurrencyServiceImpl(BinanceClient client, SubscriberRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    @Override
    public double getBitcoinPrice() throws IOException {
        if (price.get() == null) {
            price.set(client.getBitcoinPrice());
        }
        return price.get();
    }


}
