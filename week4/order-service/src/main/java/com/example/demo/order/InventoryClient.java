package com.example.demo.order;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isAvailable(Long productId) {

        String url = "http://localhost:8082/inventory/" + productId;

        InventoryResponse response = restTemplate.getForObject(url, InventoryResponse.class);

        return response.available();
    }

    record InventoryResponse(Long productId, boolean available) {
    }
}

// ⚠️ For now use:

// localhost

// Later in Docker:

// inventory-service