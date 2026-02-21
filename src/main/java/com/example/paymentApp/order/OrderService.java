package com.example.paymentapp.order;

import com.example.paymentapp.wallet.WalletService;
import com.example.paymentapp.payment.PaymentService;
import com.example.paymentapp.audit.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        .build()
        );

        paymentService.createPayment(order.getId(), amount);

        auditService.log("Order created with ID: " + order.getId());

        return order.getId();
    }
}