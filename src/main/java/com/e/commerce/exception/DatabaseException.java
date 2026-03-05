package com.e.commerce.exception;

/**
 * Exception lançada quando ocorre erro de integridade no banco de dados.
 *
 * Retorna HTTP 409 (CONFLICT)
 *
 * Exemplos de uso:
 * - Tentativa de deletar entidade com relacionamentos
 * - Violação de constraint única
 * - Violação de integridade referencial
 * - Email duplicado
 *
 * Uso típico:
 * <pre>
 * try {
 *     userRepository.deleteById(id);
 * } catch (DataIntegrityViolationException e) {
 *     throw new DatabaseException("Usuario nao pode ser deletado pois possui pedidos");
 * }
 * </pre>
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }
}


