package com.e.commerce.exception;

/**
 * Exception lançada quando o usuário não tem permissão para acessar um recurso.
 *
 * Retorna HTTP 403 (FORBIDDEN)
 *
 * Diferença entre 401 e 403:
 * - 401: Não autenticado (não logou ou token inválido)
 * - 403: Autenticado, mas sem permissão (logado mas não é admin)
 *
 * Exemplos de uso:
 * - Usuário comum tentando acessar rota de admin
 * - Usuário tentando modificar recurso de outro usuário
 * - Usuário sem role adequado para a ação
 *
 * Uso típico:
 * <pre>
 * if (!user.getRole().equals(Role.ADMIN)) {
 *     throw new ForbiddenException("Acesso negado: apenas administradores");
 * }
 * </pre>
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}


