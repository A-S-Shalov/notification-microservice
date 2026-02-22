package com.example.notification_service.mail;

import com.example.user_service.events.UserOperation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(UserOperation operation, String email) {

        String text;

        if (operation == UserOperation.CREATE) {
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else {
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Уведомление");
        message.setText(text);

        mailSender.send(message);

        System.out.println("📧 EMAIL SENT TO: " + email);
    }
}