package com.skillbox.cryptobot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.notification")
public class NotificationConfig {

    private long priceCheckInterval = 120_000;

    private long minNotificationInterval = 600_000;

}
