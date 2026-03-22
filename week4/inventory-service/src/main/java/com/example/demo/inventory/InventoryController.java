package com.example.demo.inventory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{productId}")
    public InventoryResponse checkInventory(@PathVariable Long productId) {

        boolean available = service.isAvailable(productId);

        return new InventoryResponse(productId, available);
    }

    @PostConstruct
    public void init() {
        System.out.println("🔥 InventoryController loaded");
    }

    record InventoryResponse(Long productId, boolean available) {
    }
}