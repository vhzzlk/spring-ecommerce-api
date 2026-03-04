package com.e.commerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private UUID productId;
    private String nameProduct;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
