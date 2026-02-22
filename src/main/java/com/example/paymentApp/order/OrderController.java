package com.example.paymentapp.order;

import com.example.paymentapp.order.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public CreateOrderResponse create(@Valid @RequestBody CreateOrderRequest request) {

        Long orderId = orderService.createOrder(
                request.getUserId(),
                request.getAmount());

        return new CreateOrderResponse(orderId, "CREATED");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createOrder() {
        return "Order Created Successfully";
    }
}