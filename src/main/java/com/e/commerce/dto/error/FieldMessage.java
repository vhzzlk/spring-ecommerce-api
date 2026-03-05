package com.e.commerce.dto.error;

import lombok.Getter;

/**
 * DTO que representa um campo inválido em erro de validação.
 *
 * Usado dentro de ValidationError para detalhar qual campo
 * específico falhou na validação e qual foi o erro.
 *
 * Exemplo:
 * - fieldName: "email"
 * - message: "Email invalido"
 *
 * - fieldName: "name"
 * - message: "Nome e obrigatorio"
 */
@Getter
public class FieldMessage {

    private final String fieldName;
    private final String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}

