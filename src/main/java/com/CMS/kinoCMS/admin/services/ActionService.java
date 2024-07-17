package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Action;
import com.CMS.kinoCMS.admin.repositories.ActionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public ActionService(ActionRepository actionRepository, FileUploadService fileUploadService) {
        this.actionRepository = actionRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<Action> findAll() {
        log.info("Fetching all actions");
        return actionRepository.findAll();
    }

    public Optional<Action> findById(Long id) {
        log.info("Fetching action by id: {}", id);
        return actionRepository.findById(id);
    }

    public void save(Action action) {
        log.info("Saving action with id: {}", action.getId());
        actionRepository.save(action);
    }

    public void delete(Action action) {
        log.info("Deleting action with id: {}", action.getId());
        actionRepository.delete(action);
    }

    public void saveAction(Action action, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Saving new action");
        if (file != null && !file.isEmpty()) {
            String fileName = fileUploadService.uploadFile(file);
            action.setMainImage(fileName);
            log.info("Uploaded main image for action with id: {}", action.getId());
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            action.getImages().clear();
            action.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Uploaded additional images for action with id: {}", action.getId());
        }

        action.setDateOfCreation(LocalDate.now());
        actionRepository.save(action);
        log.info("Action saved successfully with id: {}", action.getId());
    }

    public void updateAction(Long id, Action action, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Updating action with id: {}", id);
        Action existingAction = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Action not found with id " + id));

        if (file != null && !file.isEmpty()) {
            String fileName = fileUploadService.uploadFile(file);
            existingAction.setMainImage(fileName);
            log.info("Uploaded new main image for action with id: {}", id);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingAction.getImages().clear();
            existingAction.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Uploaded new additional images for action with id: {}", id);
        }

        existingAction.setName(action.getName());
        existingAction.setDescription(action.getDescription());
        existingAction.setLink(action.getLink());
        existingAction.setDescriptionSEO(action.getDescriptionSEO());
        existingAction.setKeywordsSEO(action.getKeywordsSEO());
        existingAction.setTitleSEO(action.getTitleSEO());

        actionRepository.save(existingAction);
        log.info("Action updated successfully with id: {}", id);
    }

    public void changeActionStatus(Long id) {
        log.info("Changing status for action with id: {}", id);
        Action action = findById(id).orElseThrow(() -> new IllegalArgumentException("Action not found with id " + id));
        action.setNotActive(!action.isNotActive());
        actionRepository.save(action);
        log.info("Status changed successfully for action with id: {}", id);
    }

    public List<Action> findByNotActive(boolean isNotActive) {
        log.info("Fetching actions by status: {}", isNotActive);
        return actionRepository.findByNotActive(isNotActive);
    }
}