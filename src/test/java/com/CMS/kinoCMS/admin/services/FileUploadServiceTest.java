package com.CMS.kinoCMS.admin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService fileUploadService;

    private final String uploadPath = "uploads";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(fileUploadService, "uploadPath", uploadPath);
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Test
    public void testUploadFile_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "some content".getBytes());

        String resultFilename = fileUploadService.uploadFile(file);

        assertNotNull(resultFilename);
        assertTrue(resultFilename.contains("test.txt"));

        File uploadedFile = new File(uploadPath + "/" + resultFilename);
        assertTrue(uploadedFile.exists());
        uploadedFile.delete();
    }

    @Test
    public void testUploadFile_EmptyFile() {
        MultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadFile(file));

        assertEquals("File is empty or null", exception.getMessage());
    }

    @Test
    public void testUploadFile_NullFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadFile(null));

        assertEquals("File is empty or null", exception.getMessage());
    }

    @Test
    public void testUploadFile_FileTransferException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.txt");

        // Mock file transfer failure
        FileUploadService spyService = spy(fileUploadService);
        doThrow(new IOException("File transfer failed")).when(file).transferTo(any(File.class));

        IOException exception = assertThrows(IOException.class, () -> spyService.uploadFile(file));
        assertEquals("File transfer failed", exception.getMessage());
    }

    @Test
    void testUploadFile_FailedToCreateUploadDirectory() throws IOException {
        // Устанавливаем путь, который будет вызывать ошибку создания директории
        String invalidPath = "invalid\\path";
        ReflectionTestUtils.setField(fileUploadService, "uploadPath", invalidPath);

        // Создаем мок MultipartFile
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "some content".getBytes());

        // Ожидаем IOException при вызове метода
        IOException exception = assertThrows(IOException.class, () -> fileUploadService.uploadFile(file));

        // Проверяем сообщение об ошибке
        assertEquals("Failed to create upload directory", exception.getMessage());
    }


    @Test
    public void testUploadAdditionalFiles_Success() throws IOException {
        MultipartFile file1 = new MockMultipartFile("file1", "test1.txt", "text/plain", "some content".getBytes());
        MultipartFile file2 = new MockMultipartFile("file2", "test2.txt", "text/plain", "some content".getBytes());

        MultipartFile[] files = {file1, file2};

        List<String> resultFilenames = fileUploadService.uploadAdditionalFiles(files);

        assertEquals(2, resultFilenames.size());

        for (String filename : resultFilenames) {
            File uploadedFile = new File(uploadPath + "/" + filename);
            assertTrue(uploadedFile.exists());
            uploadedFile.delete();
        }
    }

    @Test
    public void testUploadAdditionalFiles_EmptyArray() {
        MultipartFile[] files = {};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadAdditionalFiles(files));

        assertEquals("Files array is empty", exception.getMessage());
    }

    @Test
    public void testUploadAdditionalFiles_NullArray() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileUploadService.uploadAdditionalFiles(null));

        assertEquals("Files array is empty", exception.getMessage());
    }

    @Test
    public void testUploadAdditionalFiles_SingleEmptyFile() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        List<String> resultFilenames = fileUploadService.uploadAdditionalFiles(new MultipartFile[]{file});

        assertTrue(resultFilenames.isEmpty());
    }

    @Test
    public void testUploadAdditionalFiles_FileTransferException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.txt");

        // Mock file transfer failure
        FileUploadService spyService = spy(fileUploadService);
        doThrow(new IOException("File transfer failed")).when(file).transferTo(any(File.class));

        List<String> resultFilenames = spyService.uploadAdditionalFiles(new MultipartFile[]{file});

        assertTrue(resultFilenames.isEmpty());
    }
}
