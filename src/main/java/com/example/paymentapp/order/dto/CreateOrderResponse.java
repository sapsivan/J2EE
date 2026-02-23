package com.example.paymentapp.order.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    private Long orderId;
    private String status;
}