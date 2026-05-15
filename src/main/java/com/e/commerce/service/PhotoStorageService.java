package com.e.commerce.service;

import com.e.commerce.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class PhotoStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");

    private final Path uploadPath;

    public PhotoStorageService(@Value("${upload.dir:upload/photos}") String uploadDir) {
        this.uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String saveProductPhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem é obrigatório");
        }

        String extension = extractExtension(photo.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Formato de imagem inválido. Use: jpg, jpeg, png ou webp");
        }

        String newFileName = UUID.randomUUID() + extension;

        try {
            Files.createDirectories(uploadPath);
            Path destination = uploadPath.resolve(newFileName).normalize();

            if (!destination.startsWith(uploadPath)) {
                throw new IllegalArgumentException("Caminho de arquivo inválido");
            }

            Files.copy(photo.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/photos/" + newFileName;
        } catch (IOException e) {
            throw new FileStorageException("Erro ao salvar imagem do produto", e);
        }
    }

    private String extractExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Nome do arquivo inválido");
        }
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            throw new IllegalArgumentException("Arquivo sem extensão válida");
        }
        return fileName.substring(index).toLowerCase(Locale.ROOT);
    }
}
