package com.e.commerce.dto.response;

import com.e.commerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private UUID id;
    private LocalDate moment;
    private OrderStatus status;
    private String nomeCliente;
    private BigDecimal total;
    private List<OrderItemResponse> items;

}
