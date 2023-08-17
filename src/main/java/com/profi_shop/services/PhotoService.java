package com.profi_shop.services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class PhotoService {
    // Установите путь к директории, где будут храниться фотографии

    @Value("${uploads_path}")
    private String uploadDir;

    public String savePhoto(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir, filename);

        File uploadDire = new File(uploadDir);
        if(!uploadDire.exists())
            uploadDire.mkdirs();

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        BufferedImage resizedImage = resizeImageWithWhiteBackground(originalImage);

        Thumbnails.of(resizedImage)
                .outputFormat(extension)
                .outputQuality(0.8)
                .scale(1)
                .toFile(filePath.toFile());

        return filename;
    }
    private BufferedImage resizeImageWithWhiteBackground(BufferedImage originalImage) throws IOException {
        double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

        int newWidth;
        int newHeight;
        if (aspectRatio > 1) {
            newWidth = 600;
            newHeight = (int) (600 / aspectRatio);
        } else {
            newWidth = (int) (600 * aspectRatio);
            newHeight = 600;
        }

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        BufferedImage resultImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
        g2d = resultImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 600, 600);

        int x = (600 - newWidth) / 2;
        int y = (600 - newHeight) / 2;
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
