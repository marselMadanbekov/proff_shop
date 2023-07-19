package com.profi_shop.services;

import com.profi_shop.settings.Templates;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PhotoService {
    // Установите путь к директории, где будут храниться фотографии
    private static final String uploadDir = Templates.uploadDir;

    public String savePhoto(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);

        // Оптимизация и сохранение изображения с помощью Thumbnailator
        Thumbnails.of(file.getInputStream())
                .size(200, 200) // Укажите требуемые размеры изображения
                .outputQuality(0.6) // Установите желаемое качество изображения (0.0 - 1.0)
                .toFile(filePath.toFile());

        return filename;
    }

    public Resource loadPhoto(String filename) throws MalformedURLException, FileNotFoundException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("Could not load photo: " + filename);
        }
    }

    public void deletePhoto(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Files.deleteIfExists(filePath);
    }
}
