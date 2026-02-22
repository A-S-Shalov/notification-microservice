package com.example.user_service.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final String topic;

    public UserEventProducer(
            KafkaTemplate<String, UserEvent> kafkaTemplate,
            @Value("${app.kafka.user-events-topic}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(UserEvent event) {
        try {
            kafkaTemplate.send(topic, event.email(), event).get(); // ждём подтверждение от Kafka
            System.out.println("✅ SENT TO KAFKA: " + event);
        } catch (Exception e) {
            System.out.println("❌ FAILED TO SEND TO KAFKA: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}