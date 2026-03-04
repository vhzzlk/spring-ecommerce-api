package com.e.commerce.dto.request;

import jakarta.validation.constraints.Email;
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
public class UserRequest {

    @NotBlank(message = "Nome e obrigatorio")
    @Size(min = 3, max = 120, message = "Nome deve ter entre 3 e 120 caracteres")
    private String name;

    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email invalido")
    @Size(max = 120, message = "Email deve ter no maximo 120 caracteres")
    private String email;

    @NotBlank(message = "Senha e obrigatoria")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String password;

    @NotBlank(message = "Telefone e obrigatorio")
    @Size(min = 10, max = 20, message = "Telefone deve ter entre 10 e 20 caracteres")
    private String phone;
}
