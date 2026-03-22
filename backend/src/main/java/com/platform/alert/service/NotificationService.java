package com.platform.alert.service;

public interface NotificationService {
    void sendEmail(String to, String subject, String content);
    void sendSms(String phone, String content);
    void send(String channel, String target, String subject, String content);
}
