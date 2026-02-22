package com.example.notification_service.kafka;

import com.example.notification_service.mail.EmailService;
import com.example.user_service.events.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${app.kafka.user-events-topic}")
    public void onMessage(UserEvent event) {

        System.out.println("✅ GOT EVENT: " + event);

        emailService.send(event.operation(), event.email());
    }
}