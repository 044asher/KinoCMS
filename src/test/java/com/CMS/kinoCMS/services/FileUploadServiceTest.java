package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.admin.services.FileUploadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {

    private FileUploadService fileUploadService;

    @BeforeEach
    public void setup() {
        fileUploadService = new FileUploadService();
        fileUploadService.uploadPath = "test_upload_path"; // Устанавливаем путь для тестов
    }

    @Test
    public void testUploadFile() throws IOException {
        // Arrange
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());

        // Act
        String result = fileUploadService.uploadFile(mockMultipartFile);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("test.txt"));

        // Cleanup
        new File(fileUploadService.uploadPath + "/" + result).delete();
    }

    @Test
    public void testUploadFile_withNullFile() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadFile(null));
    }

    @Test
    public void testUploadFile_withEmptyFile() {
        // Arrange
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadFile(mockMultipartFile));
    }

    @Test
    public void testUploadAdditionalFiles() throws IOException {
        // Arrange
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("file1", "test1.txt", "text/plain", "Test content 1".getBytes());
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file2", "test2.txt", "text/plain", "Test content 2".getBytes());
        MultipartFile[] files = {mockMultipartFile1, mockMultipartFile2};

        // Act
        List<String> result = fileUploadService.uploadAdditionalFiles(files);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0).contains("test1.txt"));
        Assertions.assertTrue(result.get(1).contains("test2.txt"));

        // Cleanup
        for (String filename : result) {
            new File(fileUploadService.uploadPath + "/" + filename).delete();
        }
    }

    @Test
    public void testUploadAdditionalFiles_withEmptyFiles() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadAdditionalFiles(new MultipartFile[0]));
    }
}
