package com.e.commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateRequest {

    @NotNull(message = "Data do pagamento e obrigatoria")
    private LocalDate moment;
}

