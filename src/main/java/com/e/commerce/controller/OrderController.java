package com.e.commerce.controller;

import com.e.commerce.dto.request.OrderRequest;
import com.e.commerce.dto.response.OrderResponse;
import com.e.commerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;



    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(@RequestParam(required = false) UUID userId) {
        if (userId != null) {
            return ResponseEntity.ok(orderService.findByUserId(userId));
        }
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
