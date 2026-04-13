package com.e.commerce.dto.request;

import com.e.commerce.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequest {

    @NotNull(message = "Status e obrigatorio")
    private OrderStatus status;
}

