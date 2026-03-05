package com.e.commerce.dto.error;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO de erro para validações de request.
 *
 * Herda de StandardError e adiciona lista de erros específicos
 * por campo inválido.
 *
 * Usado quando:
 * - @Valid dispara MethodArgumentNotValidException
 * - Campos obrigatórios estão vazios (@NotBlank)
 * - Email está inválido (@Email)
 * - Tamanho está fora do range (@Size)
 * - Valor está abaixo do mínimo (@Min, @DecimalMin)
 *
 * Retorna HTTP 422 (UNPROCESSABLE_ENTITY)
 */
@Getter
public class ValidationError extends StandardError {

    private final List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}

