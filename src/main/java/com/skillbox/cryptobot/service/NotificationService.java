package com.skillbox.cryptobot.service;

public interface NotificationService {
    void checkPriceAndNotify();
    void cleanupOldNotifications();
}
