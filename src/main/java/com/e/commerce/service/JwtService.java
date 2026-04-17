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
 * Encapsula a emissão, leitura e validação de JWT.
 *
 * <p>O token utiliza o e-mail como subject e assinatura HMAC baseada em chave
 * externa configurada via propriedades da aplicação.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extrai o subject do token, que representa o identificador principal do usuário.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai uma claim específica do token assinado.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera um JWT sem claims adicionais.
     *
     * @param email e-mail usado como subject
     * @return token assinado
     */
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    /**
     * Gera um JWT com claims adicionais.
     */
    public String generateToken(Map<String, Object> extraClaims, String email) {
        return buildToken(extraClaims, email, jwtExpiration);
    }

    /**
     * Retorna o tempo de expiração configurado, em milissegundos.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Monta e assina o token com data de emissão e expiração.
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
     * Valida correspondência entre subject e expiração.
     */
    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email)) && !isTokenExpired(token);
    }

    /**
     * Verifica se a data de expiração já foi atingida.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai o instante de expiração do token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Faz o parse do token e valida a assinatura antes de expor as claims.
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
     * Converte a chave configurada para o formato exigido pelo JJWT.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

