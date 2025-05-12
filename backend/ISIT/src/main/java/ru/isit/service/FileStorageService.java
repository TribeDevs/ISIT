package ru.isit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff");
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/tiff"
    );
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, UUID id) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String mimeType = file.getContentType();

        if (!isAllowedExtension(fileExtension)) {
            throw new IOException("Invalid file extension. Only image files are allowed.");
        }

        if (!isAllowedMimeType(mimeType)) {
            throw new IOException("Invalid MIME type. Only image files are allowed.");
        }

        String newFileName = generateFileName(id) + "." + fileExtension;

        Path path = Paths.get(uploadDir, newFileName);
        System.out.println("Saving file to: " + path.toString());

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Directory created: " + uploadDir);
            } else {
                System.err.println("Failed to create directory: " + uploadDir);
                throw new IOException("Failed to create directory: " + uploadDir);
            }
        }

        Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return path.toString();
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf('.') != -1) {
            return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        }
        return "";
    }

    private boolean isAllowedExtension(String fileExtension) {
        return ALLOWED_EXTENSIONS.contains(fileExtension);
    }

    private boolean isAllowedMimeType(String mimeType) {
        return ALLOWED_MIME_TYPES.contains(mimeType);
    }

    private String generateFileName(UUID userId) {
        return "avatar_" + userId.toString();
    }
}
