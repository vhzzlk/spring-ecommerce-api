package com.e.commerce.service;

import com.e.commerce.dto.request.ProductRequest;
import com.e.commerce.dto.response.CategoryResponse;
import com.e.commerce.dto.response.ProductResponse;
import com.e.commerce.entity.Category;
import com.e.commerce.entity.Product;
import com.e.commerce.exception.DatabaseException;
import com.e.commerce.exception.ResourceNotFoundException;
import com.e.commerce.repository.CategoryRepository;
import com.e.commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServer {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado"));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        copyRequestToEntity(request, product);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado"));

        copyRequestToEntity(request, product);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto nao encontrado");
        }

        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Produto nao pode ser removido pois esta vinculado a pedidos");
        }
    }

    private void copyRequestToEntity(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());

        Set<Category> categories = new LinkedHashSet<>();
        Arrays.stream(request.getCategories())
                .map(String::trim)
                .filter(name -> !name.isBlank())
                .forEach(name -> {
                    Category category = categoryRepository.findByNameIgnoreCase(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoria nao encontrada: " + name));
                    categories.add(category);
                });

        if (categories.isEmpty()) {
            throw new DatabaseException("Informe ao menos uma categoria valida");
        }

        product.setCategories(categories);
    }

    private ProductResponse toResponse(Product product) {
        List<CategoryResponse> categories = product.getCategories().stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                categories
        );
    }
}
