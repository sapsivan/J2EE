package com.example.demo.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryStore store;
    private final Random random = new Random();

    public boolean isAvailable(Long productId) {

        // simulate random service failure
        if (random.nextInt(10) < 4) {
            throw new RuntimeException("Simulated inventory failure");
        }

        int quantity = store.getQuantity(productId);

        return quantity > 0;
    }
}