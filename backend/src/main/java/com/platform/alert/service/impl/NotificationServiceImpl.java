package com.platform.alert.service.impl;

import com.platform.alert.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@example.com}")
    private String fromEmail;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("邮件发送成功: to={}, subject={}", to, subject);
        } catch (Exception e) {
            log.error("邮件发送失败: to={}, error={}", to, e.getMessage());
        }
    }

    @Override
    public void sendSms(String phone, String content) {
        log.info("短信发送: phone={}, content={}", phone, content);
    }

    @Override
    public void send(String channel, String target, String subject, String content) {
        switch (channel.toUpperCase()) {
            case "EMAIL":
                sendEmail(target, subject, content);
                break;
            case "SMS":
                sendSms(target, content);
                break;
            default:
                log.warn("不支持的通知渠道: {}", channel);
        }
    }
}
