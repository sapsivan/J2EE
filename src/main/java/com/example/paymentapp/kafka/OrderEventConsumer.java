package com.example.paymentapp.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void consume(String message) {
        System.out.println("Received order event: " + message);

        // simulate processing
        if (message.contains("FAIL")) {
            throw new RuntimeException("Simulated failure");
        }

        System.out.println("Processed successfully");
    }
}