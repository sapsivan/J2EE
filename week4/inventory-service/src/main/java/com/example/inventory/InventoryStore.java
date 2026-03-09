package com.example.inventory.inventory;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InventoryStore {

    private final Map<Long, Integer> inventory = new ConcurrentHashMap<>();

    public InventoryStore() {
        inventory.put(1L, 10);
        inventory.put(2L, 0);
        inventory.put(3L, 5);
    }

    public int getQuantity(Long productId) {
        return inventory.getOrDefault(productId, 0);
    }
}