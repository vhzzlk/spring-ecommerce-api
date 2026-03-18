package com.e.commerce.controller;

import com.e.commerce.dto.response.PaymentResponse;
import com.e.commerce.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void shouldReturnPayments() throws Exception {
        PaymentResponse response = new PaymentResponse(UUID.randomUUID(), LocalDate.now(), UUID.randomUUID());
        when(paymentService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").exists());
    }

    @Test
    void shouldReturnPaymentByOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        PaymentResponse response = new PaymentResponse(UUID.randomUUID(), LocalDate.now(), orderId);
        when(paymentService.findByOrderId(eq(orderId))).thenReturn(response);

        mockMvc.perform(get("/payments/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()));
    }

    @Test
    void shouldCreatePayment() throws Exception {
        UUID orderId = UUID.randomUUID();
        PaymentResponse response = new PaymentResponse(UUID.randomUUID(), LocalDate.now(), orderId);
        when(paymentService.create(any())).thenReturn(response);

        String body = """
                {
                  "orderId": "%s"
                }
                """.formatted(orderId);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()));
    }
}

