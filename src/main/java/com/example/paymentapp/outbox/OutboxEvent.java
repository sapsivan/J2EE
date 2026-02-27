package com.example.paymentapp.outbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType; // ORDER
    private Long aggregateId; // orderId

    private String eventType; // ORDER_CREATED

    @Column(columnDefinition = "TEXT")
    private String payload;

    private boolean processed;

    private LocalDateTime createdAt;
}