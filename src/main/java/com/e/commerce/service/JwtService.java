package com.e.commerce.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável por gerenciar tokens JWT.
 *
 * Funções:
 * - Gerar tokens JWT
 * - Validar tokens JWT
 * - Extrair informações do token (email, expiração, etc)
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extrai o email (subject) do token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai uma claim específica do token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera um token JWT para o usuário.
     *
     * @param email Email do usuário
     * @return Token JWT gerado
     */
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    /**
     * Gera token JWT com claims customizadas.
     */
    public String generateToken(Map<String, Object> extraClaims, String email) {
        return buildToken(extraClaims, email, jwtExpiration);
    }

    /**
     * Retorna o tempo de expiração em milissegundos.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Constrói o token JWT.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            String email,
            long expiration
    ) {
        long now = System.currentTimeMillis();
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Valida se o token é válido para o usuário.
     */
    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email)) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token expirou.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai todas as claims do token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtém a chave de assinatura a partir da secret key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

