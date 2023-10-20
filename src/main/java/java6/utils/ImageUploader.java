package java6.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUploader {

    private static final String IMG_DIR = "src/main/resources/static/assets/img/uploads/";

    public static String saveImage(MultipartFile imageFile, String fileName) throws IOException {
        String originalFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());

        if (originalFileName.isEmpty()) {
            return null; // Không lưu nếu không có tên tệp
        }

        String fileExtension = getFileExtension(originalFileName);
        String finalFileName = fileName + fileExtension;

        Path imagePath = Paths.get(IMG_DIR + originalFileName);
        Files.copy(imageFile.getInputStream(), imagePath);

        return originalFileName; // Trả về đường dẫn tới ảnh đã lưu
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
}

