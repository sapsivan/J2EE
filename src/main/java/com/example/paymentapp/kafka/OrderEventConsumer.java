package com.example.paymentapp.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = "order-events", groupId = "order-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        log.info("Received order event: {}", message);

        // simulate processing
        if (message.contains("FAIL")) {
            throw new RuntimeException("Simulated failure");
        }

        log.info("Processed event {} successfully", message);
    }
}