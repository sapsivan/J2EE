package com.example.paymentapp2.order.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    private Long orderId;
    private String status;
}