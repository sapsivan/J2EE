package com.example.paymentapp.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publish() {

        List<OutboxEvent> events = outboxRepository.findTop10ByProcessedFalseOrderByCreatedAtAsc();

        for (OutboxEvent event : events) {

            kafkaTemplate.send("order-events",
                    event.getAggregateId().toString(),
                    event.getPayload());

            event.setProcessed(true);
            outboxRepository.save(event);

            System.out.println("Published event: " + event.getId());
        }
    }
}