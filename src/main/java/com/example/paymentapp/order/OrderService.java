package com.example.paymentapp.order;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.css.Counter;

import com.example.paymentapp.audit.AuditService;
import com.example.paymentapp.payment.PaymentService;
import com.example.paymentapp.wallet.WalletService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final WalletService walletService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final AuditService auditService;

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
        private final Counter orderFetchCounter;

    public OrderService(MeterRegistry registry) {
        this.orderFetchCounter = registry.counter("orders.fetch.count");
    }


    @Cacheable(value = "orders", key = "#id")
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @CacheEvict(value = "orders", key = "#id")
    public OrderDto updateOrder(Long id, UpdateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow();

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        return OrderMapper.toDto(order);
    }
}