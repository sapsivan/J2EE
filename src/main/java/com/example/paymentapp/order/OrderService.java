package com.example.paymentapp.order;

import lombok.RequiredArgsConstructor;
import com.example.paymentapp.order.OrderEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentapp.order.dto.OrderDto;
import com.example.paymentapp.order.dto.UpdateOrderRequest;
import com.example.paymentapp.audit.AuditService;
import com.example.paymentapp.payment.PaymentService;
import com.example.paymentapp.wallet.WalletService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import io.micrometer.core.instrument.Counter;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final WalletService walletService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final AuditService auditService;
    private final MeterRegistry meterRegistry;

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

        paymentService.createPayment(order.getId(), amount);

        auditService.log("Order created with ID: " + order.getId());

        return order.getId();
    }

    @Cacheable(value = "orders", key = "#id")
    public OrderDto getOrderById(Long id) {
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