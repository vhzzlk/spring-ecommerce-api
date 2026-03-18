package com.e.commerce.controller;

import com.e.commerce.dto.response.OrderResponse;
import com.e.commerce.enums.OrderStatus;
import com.e.commerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturnOrdersByUserWhenUserIdIsProvided() throws Exception {
        UUID userId = UUID.randomUUID();
        OrderResponse response = new OrderResponse(UUID.randomUUID(), LocalDate.now(), OrderStatus.AGUARDANDO_PAGAMENTO,
                "Carlos", new BigDecimal("99.90"), List.of());

        when(orderService.findByUserId(eq(userId))).thenReturn(List.of(response));

        mockMvc.perform(get("/orders").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCliente").value("Carlos"));
    }

    @Test
    void shouldCreateOrder() throws Exception {
        OrderResponse response = new OrderResponse(UUID.randomUUID(), LocalDate.now(), OrderStatus.AGUARDANDO_PAGAMENTO,
                "Ana", new BigDecimal("150.00"), List.of());

        when(orderService.create(any())).thenReturn(response);

        String body = """
                {
                  "userId": "%s",
                  "items": [
                    {
                      "productId": "%s",
                      "quantity": 2
                    }
                  ]
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCliente").value("Ana"));
    }
}

