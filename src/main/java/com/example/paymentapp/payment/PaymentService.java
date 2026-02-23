package com.example.paymentapp.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createPayment(Long orderId, BigDecimal amount) {

        paymentRepository.save(
                PaymentEntity.builder()
                        .orderId(orderId)
                        .amount(amount)
                        .status("SUCCESS")
                        .build());
    }
}