package com.example.inventory.inventory;

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

    record InventoryResponse(Long productId, boolean available) {
    }
}