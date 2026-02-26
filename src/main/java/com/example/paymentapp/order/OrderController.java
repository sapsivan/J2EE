package com.example.paymentapp.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.paymentapp.order.dto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

        private final OrderService orderService;

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<CreateOrderResponse> create(
                        @Valid @RequestBody CreateOrderRequest request) {
                // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                // String username = auth.getName();

                // System.out.println(username);

                Long orderId = orderService.createOrder(
                                request.getUserId(),
                                request.getAmount());

                return ResponseEntity.ok(
                                new CreateOrderResponse(orderId, "CREATED"));
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {

                OrderDto dto = orderService.getOrderById(id);

                return ResponseEntity.ok(dto);
        }
}