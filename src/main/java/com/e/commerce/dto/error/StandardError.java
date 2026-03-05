package com.e.commerce.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * DTO de erro padrão da API.
 *
 * Representa qualquer erro genérico retornado pela aplicação.
 * Usado para erros como:
 * - Recurso não encontrado (404)
 * - Erro de banco de dados (409)
 * - Não autorizado (401)
 * - Proibido (403)
 * - Erro inesperado (500)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}

