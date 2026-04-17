package com.e.commerce.config;

import com.e.commerce.service.JwtService;
import com.e.commerce.entity.User;
import com.e.commerce.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que valida JWT (JSON Web Token) antes de processar requisição.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Extrair token do header Authorization (formato: Bearer &lt;token&gt;)</li>
 *   <li>Validar assinatura e expiração do token</li>
 *   <li>Buscar usuário correspondente no banco</li>
 *   <li>Estabelecer contexto de segurança (SecurityContext)</li>
 *   <li>Permitir execução do endpoint se token válido</li>
 *   <li>Bloquear requisição se token inválido/expirado</li>
 * </ul>
 *
 * <p>Flow:
 * <ol>
 *   <li>Cliente inclui: Authorization: Bearer &lt;jwt_token&gt;</li>
 *   <li>Filtro extrai o token do header</li>
 *   <li>Valida usando JwtService</li>
 *   <li>Se válido: busca User no banco e cria Authentication</li>
 *   <li>Se inválido: passa para próximo filtro (será rejeitado por @PreAuthorize)</li>
 * </ol>
 *
 * <p>Segurança:
 * <ul>
 *   <li>Valida assinatura HMAC do token</li>
 *   <li>Verifica expiração do token</li>
 *   <li>Confirma que email no token corresponde ao usuário no banco</li>
 * </ul>
 *
 * @author E-Commerce Team
 * @version 1.0
 * @since 2026-04-17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * Processa o filtro uma única vez por requisição.
     *
     * <p>Extrai, valida e processa JWT do header Authorization.
     * Se token válido, estabelece contexto de segurança para a requisição.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @param filterChain cadeia de filtros
     * @throws ServletException se erro ao processar
     * @throws IOException se erro de I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Extrair token do header
            String authHeader = request.getHeader("Authorization");
            
            // Se header não existe ou não começa com "Bearer ", passa para próximo filtro
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("Header Authorization não encontrado ou formato inválido");
                filterChain.doFilter(request, response);
                return;
            }
            
            // Extrair apenas o token (remover "Bearer " prefix)
            String token = authHeader.substring(7);
            log.debug("Token JWT recebido, validando...");
            
            // Extrair email (subject) do token
            String email = jwtService.extractUsername(token);
            
            // Se email foi extraído e SecurityContext ainda não foi setado
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Buscar usuário no banco
                User user = userRepository.findByEmail(email).orElse(null);
                
                // Validar: usuário existe e token é válido para este usuário
                if (user != null && jwtService.isTokenValid(token, email)) {
                    log.info("Token JWT válido para usuário: {}", email);
                    
                    // Criar objeto de autenticação com role do usuário
                    var authToken = new UsernamePasswordAuthenticationToken(
                        email,                                              // principal (email do usuário)
                        null,                                               // credentials (null pois já foi validado)
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // authorities
                    );
                    
                    // Definir autenticação no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("SecurityContext setado para usuário: {} com role: {}", email, user.getRole());
                    
                } else {
                    // Token inválido, expirado ou usuário não encontrado
                    log.warn("Token inválido ou usuário não encontrado: {}", email);
                    SecurityContextHolder.clearContext();
                }
            }
            
            // Passar para próximo filtro na cadeia
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            log.error("Erro ao processar JWT", e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }
}

