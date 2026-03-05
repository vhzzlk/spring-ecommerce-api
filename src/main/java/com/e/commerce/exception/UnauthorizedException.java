package com.e.commerce.exception;

/**
 * Exception lançada quando o usuário não está autenticado.
 *
 * Retorna HTTP 401 (UNAUTHORIZED)
 *
 * Exemplos de uso:
 * - Token JWT inválido ou expirado
 * - Token JWT ausente
 * - Credenciais inválidas (email/senha incorretos)
 * - Tentativa de acessar endpoint protegido sem autenticação
 *
 * Uso típico:
 * <pre>
 * if (!passwordEncoder.matches(password, user.getPassword())) {
 *     throw new UnauthorizedException("Credenciais invalidas");
 * }
 * </pre>
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}


