package com.e.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.e.commerce.service.CustomUserDetailsService;

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

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

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
         * Regras atuais:
     * 1. Apenas usuário NÃO autenticado acessa /auth/login e /auth/register
     * 2. Endpoints de escrita de catálogo exigem role ADMIN
     * 3. Demais endpoints exigem autenticação
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF (não precisa em API REST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").anonymous()
                        .requestMatchers(HttpMethod.POST, "/categories/**", "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**", "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**", "/products/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sem sessão, usa JWT
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

