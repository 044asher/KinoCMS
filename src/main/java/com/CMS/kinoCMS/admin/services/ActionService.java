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
        log.info("Start ActionService - findAll");
        List<Action> actions = actionRepository.findAll();
        log.info("Successfully executed ActionService - findAll");
        return actions;
    }

    public Optional<Action> findById(Long id) {
        log.info("Start ActionService - findById for id: {}", id);
        Optional<Action> action = actionRepository.findById(id);
        log.info("Successfully executed ActionService - findById for id: {}", id);
        return action;
    }

    public void save(Action action) {
        log.info("Start ActionService - save for action: {}", action);
        actionRepository.save(action);
        log.info("Successfully executed ActionService - save for action: {}", action);
    }

    public void delete(Action action) {
        log.info("Start ActionService - delete for action: {}", action);
        actionRepository.delete(action);
        log.info("Successfully executed ActionService - delete for action: {}", action);
    }

    public void saveAction(Action action, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start ActionService - saveAction for action: {}", action);
        try {
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
            log.info("Successfully executed ActionService - saveAction for action: {}", action);
        } catch (IOException e) {
            log.error("Failed to execute ActionService - saveAction for action: {}", action, e);
            throw e;
        }
    }

    public void updateAction(Long id, Action action, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start ActionService - updateAction for id: {}", id);
        try {
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
            log.info("Successfully executed ActionService - updateAction for id: {}", id);
        } catch (IOException e) {
            log.error("Failed to execute ActionService - updateAction for id: {}", id, e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Action not found for id: {}", id, e);
            throw e;
        }
    }

    public void changeActionStatus(Long id) {
        log.info("Start ActionService - changeActionStatus for id: {}", id);
        try {
            Action action = findById(id).orElseThrow(() -> new IllegalArgumentException("Action not found with id " + id));
            action.setNotActive(!action.isNotActive());
            actionRepository.save(action);
            log.info("Successfully executed ActionService - changeActionStatus for id: {}", id);
        } catch (IllegalArgumentException e) {
            log.error("Failed to execute ActionService - changeActionStatus for id: {}", id, e);
            throw e;
        }
    }

    public List<Action> findByNotActive(boolean isNotActive) {
        log.info("Start ActionService - findByNotActive for isNotActive: {}", isNotActive);
        List<Action> actions = actionRepository.findByNotActive(isNotActive);
        log.info("Successfully executed ActionService - findByNotActive for isNotActive: {}", isNotActive);
        return actions;
    }
}