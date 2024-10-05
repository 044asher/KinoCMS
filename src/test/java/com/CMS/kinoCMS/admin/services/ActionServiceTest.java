package com.CMS.kinoCMS.admin.services;


import com.CMS.kinoCMS.models.Action;
import com.CMS.kinoCMS.repositories.ActionRepository;
import com.CMS.kinoCMS.services.ActionService;
import com.CMS.kinoCMS.services.FileUploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private ActionService actionService;

    @Test
    void testFindAll() {
        List<Action> actions = List.of(new Action(), new Action());
        when(actionRepository.findAll()).thenReturn(actions);

        List<Action> result = actionService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(actionRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Action action = new Action();
        when(actionRepository.findById(1L)).thenReturn(Optional.of(action));

        Optional<Action> result = actionService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(action, result.get());
        verify(actionRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Action action = new Action();

        actionService.save(action);

        verify(actionRepository, times(1)).save(action);
    }

    @Test
    void testDelete() {
        Action action = new Action();

        actionService.delete(action);

        verify(actionRepository, times(1)).delete(action);
    }

    @Test
    void testSaveAction() throws IOException {
        Action action = new Action();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenReturn("mainImage.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        actionService.saveAction(action, file, additionalFiles);

        assertEquals("mainImage.jpg", action.getMainImage());
        assertEquals(2, action.getImages().size());
        assertEquals(LocalDate.now(), action.getDateOfCreation());
        verify(actionRepository, times(1)).save(action);
    }

    @Test
    void testUpdateAction_ActionNotFound() throws IOException {
        Action updatedAction = new Action();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class)};

        when(actionRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> actionService.updateAction(1L, updatedAction, file, additionalFiles));
        assertEquals("Action not found with id 1", exception.getMessage());

        verify(fileUploadService, times(0)).uploadFile(file);
        verify(actionRepository, times(0)).save(any(Action.class));
    }


    @Test
    void testSaveAction_FileUploadException() throws IOException {
        Action action = new Action();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class)};
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenThrow(new IOException("Upload failed"));

        IOException exception = assertThrows(IOException.class, () -> actionService.saveAction(action, file, additionalFiles));
        assertEquals("Upload failed", exception.getMessage());
        verify(fileUploadService, times(1)).uploadFile(file);
        verify(actionRepository, times(0)).save(action);
    }

    @Test
    void testUpdateAction() throws IOException {
        Action existingAction = new Action();
        existingAction.setId(1L);
        Action updatedAction = new Action();
        updatedAction.setName("Updated Name");
        updatedAction.setDescription("Updated Description");
        updatedAction.setLink("Updated Link");
        updatedAction.setDescriptionSEO("Updated Description SEO");
        updatedAction.setKeywordsSEO("Updated Keywords SEO");
        updatedAction.setTitleSEO("Updated Title SEO");

        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(actionRepository.findById(1L)).thenReturn(Optional.of(existingAction));
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenReturn("mainImage.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        actionService.updateAction(1L, updatedAction, file, additionalFiles);

        assertEquals("Updated Name", existingAction.getName());
        assertEquals("Updated Description", existingAction.getDescription());
        assertEquals("Updated Link", existingAction.getLink());
        assertEquals("Updated Description SEO", existingAction.getDescriptionSEO());
        assertEquals("Updated Keywords SEO", existingAction.getKeywordsSEO());
        assertEquals("Updated Title SEO", existingAction.getTitleSEO());
        assertEquals("mainImage.jpg", existingAction.getMainImage());
        assertEquals(2, existingAction.getImages().size());
        verify(actionRepository, times(1)).findById(1L);
        verify(actionRepository, times(1)).save(existingAction);
    }

    @Test
    void testUpdateAction_FileUploadException() throws IOException {
        Action existingAction = new Action();
        existingAction.setId(1L);
        Action updatedAction = new Action();
        MultipartFile file = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class)};
        when(actionRepository.findById(1L)).thenReturn(Optional.of(existingAction));
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(file)).thenThrow(new IOException("Upload failed"));

        IOException exception = assertThrows(IOException.class, () -> actionService.updateAction(1L, updatedAction, file, additionalFiles));
        assertEquals("Upload failed", exception.getMessage());
        verify(actionRepository, times(1)).findById(1L);
        verify(fileUploadService, times(1)).uploadFile(file);
        verify(actionRepository, times(0)).save(existingAction);
    }

    @Test
    void testChangeActionStatus() {
        Action action = new Action();
        action.setNotActive(true);
        when(actionRepository.findById(1L)).thenReturn(Optional.of(action));

        actionService.changeActionStatus(1L);

        assertFalse(action.isNotActive());
        verify(actionRepository, times(1)).findById(1L);
        verify(actionRepository, times(1)).save(action);
    }

    @Test
    void testChangeActionStatus_ActionNotFound() {
        when(actionRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> actionService.changeActionStatus(1L));
        assertEquals("Action not found with id 1", exception.getMessage());
        verify(actionRepository, times(1)).findById(1L);
        verify(actionRepository, times(0)).save(any(Action.class));
    }

    @Test
    void testFindByNotActive() {
        List<Action> actions = List.of(new Action(), new Action());
        when(actionRepository.findByNotActive(true)).thenReturn(actions);

        List<Action> result = actionService.findByNotActive(true);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(actionRepository, times(1)).findByNotActive(true);
    }
}
