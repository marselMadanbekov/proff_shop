package com.profi_shop.services;

import com.profi_shop.settings.Templates;
import net.coobird.thumbnailator.Thumbnails;
import org.imgscalr.Scalr;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir, filename);

        // Получение загруженного изображения
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // Создание нового изображения с белым фоном и подгонкой исходного изображения
        BufferedImage resizedImage = resizeImageWithWhiteBackground(originalImage, 600, 600);

        // Сохранение изображения
        Thumbnails.of(resizedImage)
                .outputFormat(extension)
                .outputQuality(0.8)
                .scale(1)
                .toFile(filePath.toFile());

        return filename;
    }
    private BufferedImage resizeImageWithWhiteBackground(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

        int newWidth;
        int newHeight;
        if (aspectRatio > 1) {
            newWidth = targetWidth;
            newHeight = (int) (targetWidth / aspectRatio);
        } else {
            newWidth = (int) (targetHeight * aspectRatio);
            newHeight = targetHeight;
        }

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        BufferedImage resultImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        g2d = resultImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);

        int x = (targetWidth - newWidth) / 2;
        int y = (targetHeight - newHeight) / 2;
        g2d.drawImage(scaledImage, x, y, null);
        g2d.dispose();

        return resultImage;
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
