package com.ecommerce.service;

import com.ecommerce.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${project.image.upload.dir}")
    private String imageUploadPath;

    @Override
    public String uploadImage(MultipartFile file) {
        // Validate file type is images
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new ApiException("Only images are allowed");
        }

        // File name of current / original image
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String path = imageUploadPath + File.separator + fileName;

        // Check if path exists, if not create the directory
        File folder = new File(imageUploadPath);
        if (!folder.exists()) {
            System.out.println("Creating directory to upload image");
            boolean created = folder.mkdirs();
            if (!created) {
                throw new ApiException("Failed to create directory to upload image");
            }
        }

        // Copy the image to the directory
        try {
            Files.copy(file.getInputStream(), Paths.get(path));
        } catch (IOException e) {
            System.out.println("Failed to upload image " + e);
            throw new ApiException("Failed to upload image " + e.getMessage());
        }

        return fileName;
    }

    @Override
    public void deleteImage(String fileName) {
        String path = imageUploadPath + File.separator + fileName;
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete file");
            }
        } else {
            System.out.println("File not found");
        }
    }
}
