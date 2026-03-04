package com.e.commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Nome da categoria e obrigatorio")
    @Size(min = 2, max = 80, message = "Nome da categoria deve ter entre 2 e 80 caracteres")
    private String name;
}
