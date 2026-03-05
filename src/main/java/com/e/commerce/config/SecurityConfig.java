package com.e.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação.
 *
 * Responsabilidades:
 * 1. Configurar BCrypt para criptografia de senhas
 * 2. Configurar endpoints públicos e privados
 * 3. Desabilitar CSRF (não necessário em API REST stateless)
 * 4. Configurar sessão stateless (usa JWT ao invés de session)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean de PasswordEncoder usando BCrypt.
     *
     * BCrypt é um algoritmo de hash muito seguro:
     * - Adiciona "salt" automático (torna cada hash único)
     * - É lento de propósito (dificulta ataques de força bruta)
     * - Gera hashs diferentes para a mesma senha
     *
     * Exemplo:
     * senha "123456" → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
     * senha "123456" → $2a$10$X2e/8fBGlHq.8bVGJ6Q7O.sXpEn7gZz5Q7r0FtLnVqYKQVZ8gk6sm
     * (Hashs diferentes, mas matches() funciona para ambos!)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuração da cadeia de filtros de segurança.
     *
     * Por enquanto, permite acesso a todos os endpoints.
     * Futuramente você vai:
     * 1. Adicionar filtro JWT
     * 2. Proteger endpoints específicos
     * 3. Exigir roles (ADMIN, USER)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (não precisa em API REST)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Por enquanto, libera tudo
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sem sessão, usa JWT
                );

        return http.build();
    }
}

