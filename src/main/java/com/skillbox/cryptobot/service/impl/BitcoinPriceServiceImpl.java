package com.skillbox.cryptobot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.cryptobot.service.BitcoinPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class BitcoinPriceServiceImpl implements BitcoinPriceService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price";

    @Override
    public Double getBitcoinPrice() {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(BINANCE_API_URL)
                    .queryParam("symbol", "BITCUSDT")
                    .build()
                    .toUri();

            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("price").asDouble();
            }

        } catch (Exception exception) {
            log.error("Ошибка при получении цены биткоина", exception);
        }
        return null;
    }

}
