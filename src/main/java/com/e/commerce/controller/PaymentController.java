package com.e.commerce.controller;

import com.e.commerce.dto.request.PaymentRequest;
import com.e.commerce.dto.request.PaymentUpdateRequest;
import com.e.commerce.dto.response.PaymentResponse;
import com.e.commerce.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponse>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/find-by-order/{orderId}")
    public ResponseEntity<PaymentResponse> findByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.findByOrderId(orderId));
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PaymentResponse> update(@PathVariable UUID id, @Valid @RequestBody PaymentUpdateRequest request) {
        return ResponseEntity.ok(paymentService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
