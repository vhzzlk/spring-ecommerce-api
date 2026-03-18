package com.e.commerce.controller;

import com.e.commerce.dto.response.LoginResponse;
import com.e.commerce.dto.response.UserResponse;
import com.e.commerce.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void shouldLogin() throws Exception {
        LoginResponse response = new LoginResponse("token-teste", "Bearer", 3600L);
        when(authService.login(any())).thenReturn(response);

        String body = """
                {
                  "email": "admin@mail.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-teste"));
    }

    @Test
    void shouldRegister() throws Exception {
        UserResponse response = new UserResponse(UUID.randomUUID(), "Admin", "admin@mail.com", "11977777777");
        when(authService.register(any())).thenReturn(response);

        String body = """
                {
                  "name": "Admin",
                  "email": "admin@mail.com",
                  "password": "123456",
                  "phone": "11977777777"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin@mail.com"));
    }
}

