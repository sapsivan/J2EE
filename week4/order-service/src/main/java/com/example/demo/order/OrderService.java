package com.example.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryClient inventoryClient;

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    public String placeOrder(Long productId) {

        boolean available = inventoryClient.isAvailable(productId);

        if (!available) {
            return "REJECTED - OUT OF STOCK";
        }

        return "ORDER CREATED";
    }

    public String fallback(Long productId, Throwable throwable) {
        return "REJECTED - SERVICE UNAVAILABLE";
    }
}