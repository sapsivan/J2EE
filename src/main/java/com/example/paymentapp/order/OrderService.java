package com.example.paymentapp.order;

import lombok.RequiredArgsConstructor;
import com.example.paymentapp.order.OrderEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentapp.order.dto.OrderDto;
import com.example.paymentapp.order.dto.UpdateOrderRequest;
import com.example.paymentapp.outbox.OutboxEvent;
import com.example.paymentapp.audit.AuditService;
import com.example.paymentapp.payment.PaymentService;
import com.example.paymentapp.wallet.WalletService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import com.example.paymentapp.outbox.OutboxRepository;
import io.micrometer.core.instrument.Counter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final WalletService walletService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final AuditService auditService;
    private final MeterRegistry meterRegistry;
    private final OutboxRepository outboxRepository;

    private Counter orderFetchCounter;

    @PostConstruct
    public void init() {
        this.orderFetchCounter = meterRegistry.counter("orders.fetch.count");
    }

    @Transactional
    public Long createOrder(Long userId, BigDecimal amount) {

        walletService.deduct(userId, amount);

        OrderEntity order = orderRepository.save(
                OrderEntity.builder()
                        .userId(userId)
                        .amount(amount)
                        .status("CREATED")
                        .build());

        // paymentService.createPayment(order.getId(), amount);

        // auditService.log("Order created with ID: " + order.getId());

        // Create Outbox event in SAME transaction
        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("ORDER")
                .aggregateId(order.getId())
                .eventType("ORDER_CREATED")
                .payload("{\"orderId\":" + order.getId() + "}")
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();

        outboxRepository.save(event);

        return order.getId();
    }

    @Cacheable(value = "orders", key = "#id", sync = true)
    public OrderDto getOrderById(Long id) {

        System.out.println("Fetching from DB for id " + id);

        return orderRepository.findById(id)
                .map(order -> new OrderDto(order.getId(), order.getStatus()))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @CacheEvict(value = "orders", key = "#id")
    public OrderDto updateOrder(Long id, UpdateOrderRequest request) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow();

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        return dto;
    }

    @CacheEvict(value = "orders", key = "#id")
    public void markAsPaid(Long id) {

        System.out.println("Updating DB for order " + id);

        // simulate update
        OrderEntity order = orderRepository.findById(id).orElseThrow();
        order.setStatus("PAID");
        orderRepository.save(order);
    }
}