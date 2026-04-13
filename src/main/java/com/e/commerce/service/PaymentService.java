package com.e.commerce.service;

import com.e.commerce.dto.request.PaymentRequest;
import com.e.commerce.dto.request.PaymentUpdateRequest;
import com.e.commerce.dto.response.PaymentResponse;
import com.e.commerce.entity.Order;
import com.e.commerce.entity.Payment;
import com.e.commerce.enums.OrderStatus;
import com.e.commerce.exception.DatabaseException;
import com.e.commerce.exception.ResourceNotFoundException;
import com.e.commerce.repository.OrderRepository;
import com.e.commerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse findById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento nao encontrado"));
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse findByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento nao encontrado para o pedido informado"));
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido nao encontrado"));

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new DatabaseException("Pedido ja possui pagamento registrado");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMoment(LocalDate.now());

        order.setPayment(payment);
        order.setStatus(OrderStatus.PAGO);

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse update(UUID id, PaymentUpdateRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento nao encontrado"));

        payment.setMoment(request.getMoment());
        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public void delete(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pagamento nao encontrado");
        }
        paymentRepository.deleteById(id);
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getMoment(),
                payment.getOrder().getId()
        );
    }
}
