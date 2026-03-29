package com.example.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryClient inventoryClient;

    public String placeOrder(Long productId) {

        boolean available = inventoryClient.isAvailable(productId);

        if (!available) {
            return "REJECTED - OUT OF STOCK";
        }

        return "ORDER CREATED";
    }
}