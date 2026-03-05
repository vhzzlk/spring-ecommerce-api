package com.e.commerce.exception;

/**
 * Exception lançada quando um recurso não é encontrado no sistema.
 *
 * Retorna HTTP 404 (NOT_FOUND)
 *
 * Exemplos de uso:
 * - Usuário não encontrado por ID
 * - Produto não encontrado por ID
 * - Pedido não encontrado por ID
 * - Categoria não encontrada por ID
 * - Pagamento não encontrado por ID
 *
 * Uso típico:
 * <pre>
 * Product product = productRepository.findById(id)
 *     .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado"));
 * </pre>
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}


