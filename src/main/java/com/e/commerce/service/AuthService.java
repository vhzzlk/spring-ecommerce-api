package com.e.commerce.service;

import com.e.commerce.dto.request.LoginRequest;
import com.e.commerce.dto.request.UserRequest;
import com.e.commerce.dto.response.LoginResponse;
import com.e.commerce.dto.response.UserResponse;
import com.e.commerce.entity.User;
import com.e.commerce.enums.Role;
import com.e.commerce.exception.DatabaseException;
import com.e.commerce.exception.UnauthorizedException;
import com.e.commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação e autorização.
 *
 * Responsabilidades:
 * 1. Login - validar credenciais e gerar token JWT
 * 2. Registro - criar novo usuário com senha criptografada
 * 3. Segurança - usar BCrypt para senhas e JWT para sessões
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Realiza o login do usuário.
     *
     * Fluxo:
     * 1. Busca usuário por email
     * 2. Valida senha com BCrypt
     * 3. Gera token JWT
     * 4. Retorna LoginResponse com token
     *
     * @param request LoginRequest com email e senha
     * @return LoginResponse com token JWT
     * @throws UnauthorizedException se credenciais inválidas
     */
    public LoginResponse login(LoginRequest request) {
        // 1. Busca usuário por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciais invalidas"));

        // 2. Valida senha (compara senha enviada com hash do banco)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciais invalidas");
        }

        // 3. Gera token JWT
        String token = jwtService.generateToken(user.getEmail());

        // 4. Monta e retorna LoginResponse
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setExpiresIn(jwtService.getExpirationTime() / 1000); // Converte ms para segundos

        return response;
    }

    /**
     * Registra um novo usuário.
     *
     * Fluxo:
     * 1. Valida se email já existe
     * 2. Criptografa a senha com BCrypt
     * 3. Define role padrão (USER)
     * 4. Salva no banco
     * 5. Retorna UserResponse
     *
     * @param request UserRequest com dados do usuário
     * @return UserResponse com dados do usuário criado (sem senha)
     * @throws DatabaseException se email já cadastrado
     */
    public UserResponse register(UserRequest request) {
        // 1. Valida se email já existe
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DatabaseException("Email ja cadastrado");
        }

        // 2. Cria entidade User
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Criptografa senha
        user.setPhone(request.getPhone());
        user.setRole(Role.USER); // Define role padrão

        // 3. Salva no banco
        user = userRepository.save(user);

        // 4. Converte para UserResponse (sem retornar senha)
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());

        return response;
    }
}
