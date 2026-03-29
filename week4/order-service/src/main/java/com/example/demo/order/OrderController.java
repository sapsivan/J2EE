package com.example.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{productId}")
    public OrderResponse create(@PathVariable Long productId) {

        String status = orderService.placeOrder(productId);

        return new OrderResponse(productId, status);
    }
}