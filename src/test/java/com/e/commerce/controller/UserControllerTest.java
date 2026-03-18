package com.e.commerce.controller;

import com.e.commerce.dto.request.UserRequest;
import com.e.commerce.dto.response.UserResponse;
import com.e.commerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserResponse user = new UserResponse(UUID.randomUUID(), "Maria", "maria@mail.com", "11999999999");
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Maria"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserResponse created = new UserResponse(UUID.randomUUID(), "Joao", "joao@mail.com", "11988888888");
        when(userService.create(any(UserRequest.class))).thenReturn(created);

        String body = """
                {
                  "name": "Joao",
                  "email": "joao@mail.com",
                  "password": "123456",
                  "phone": "11988888888"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("joao@mail.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userService).delete(eq(id));

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());
    }
}
