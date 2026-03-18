package com.e.commerce.controller;

import com.e.commerce.dto.response.CategoryResponse;
import com.e.commerce.dto.response.ProductResponse;
import com.e.commerce.service.ProductServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServer productService;

    @Test
    void shouldReturnAllProducts() throws Exception {
        ProductResponse response = new ProductResponse(
                UUID.randomUUID(),
                "Mouse Gamer",
                "Mouse RGB",
                new BigDecimal("129.90"),
                "https://img.com/mouse.png",
                List.of(new CategoryResponse(UUID.randomUUID(), "Perifericos"))
        );

        when(productService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mouse Gamer"));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductResponse response = new ProductResponse(
                UUID.randomUUID(),
                "Teclado",
                "Teclado mecanico",
                new BigDecimal("299.90"),
                "https://img.com/teclado.png",
                List.of(new CategoryResponse(UUID.randomUUID(), "Perifericos"))
        );

        when(productService.create(any())).thenReturn(response);

        String body = """
                {
                  "name": "Teclado",
                  "description": "Teclado mecanico",
                  "price": 299.90,
                  "imageUrl": "https://img.com/teclado.png",
                  "categories": ["Perifericos"]
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Teclado"));
    }
}

