package backend.tangsquad.file.dto;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String dirName) throws IOException {
        File localFile = convert(file).orElseThrow(() -> new IllegalArgumentException("Failed to convert MultipartFile to File"));

        String fileName = dirName + "/" + localFile.getName();
        String uploadImageUrl = putS3(localFile, fileName);

        removeNewFile(localFile); // Delete the local file after uploading

        return uploadImageUrl; // Return the pre-signed URL
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(bucket, fileName, uploadFile);
        return generatePresignedUrl(fileName);
    }

    private String generatePresignedUrl(String fileName) {
        // Set the expiration time for the URL
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withExpiration(expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File deleted successfully.");
        } else {
            log.warn("Failed to delete file.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
