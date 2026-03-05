package com.e.commerce.controller;

import com.e.commerce.dto.request.LoginRequest;
import com.e.commerce.dto.request.UserRequest;
import com.e.commerce.dto.response.LoginResponse;
import com.e.commerce.dto.response.UserResponse;
import com.e.commerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de autenticação.
 *
 * Endpoints públicos (não exigem autenticação):
 * - POST /auth/login - Fazer login
 * - POST /auth/register - Registrar novo usuário
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint de login.
     *
     * @param request LoginRequest com email e senha
     * @return LoginResponse com token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de registro.
     *
     * @param request UserRequest com dados do usuário
     * @return UserResponse com dados do usuário criado (HTTP 201)
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

