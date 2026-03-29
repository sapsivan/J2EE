package com.example.demo.order;

public record OrderResponse(
        Long productId,
        String status) {
}