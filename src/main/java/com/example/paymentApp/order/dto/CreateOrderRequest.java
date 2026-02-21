package com.example.paymentapp.order.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull
    private Long userId;

    @NotNull
    @Positive
    private BigDecimal amount;
}