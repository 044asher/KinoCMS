package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.Action;
import com.CMS.kinoCMS.admin.services.ActionService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
import com.CMS.kinoCMS.config.MyUserDetails;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/actions")
public class ActionController {
    private static final Logger logger = LogManager.getLogger(ActionController.class);

    private final ActionService actionService;
    private final FileUploadService fileUploadService;

    @Autowired
    public ActionController(ActionService actionService, FileUploadService fileUploadService) {
        this.actionService = actionService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String actions(Model model) {
        logger.info("Entering actions method");
        model.addAttribute("actions", actionService.findAll());
        logger.info("Exiting actions method");
        return "actions/action-list";
    }

    @GetMapping("/add")
    public String actionAdd(Model model) {
        logger.info("Entering actionAdd (GET) method");
        model.addAttribute("action", new Action());
        logger.info("Exiting actionAdd (GET) method");

        return "actions/action-add";
    }

    @PostMapping("/add")
    public String actionAdd(@Valid @ModelAttribute Action action,
                            BindingResult bindingResult,
                            @RequestParam(required = false) MultipartFile file,
                            @RequestParam("additionalFiles") MultipartFile[] additionalFiles) {
        logger.info("Entering actionAdd (POST) method");
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "actions/action-add";
        }
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                action.setMainImage(fileName);
                logger.info("File uploaded successfully with name: {}", fileName);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                action.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for new action");
            }

            action.setDateOfCreation(LocalDate.now());
            actionService.save(action);
            logger.info("Action saved successfully with ID: {}", action.getId());
        } catch (IOException e) {
            logger.error("Error uploading file", e);
        }
        logger.info("Exiting actionAdd (POST) method");
        return "redirect:/admin/actions";
    }

    @GetMapping("/{id}")
    public String actionEdit(@PathVariable int id, Model model) {
        logger.info("Entering actionEdit (GET) method with ID: {}", id);
        Optional<Action> action = actionService.findById(id);
        if (action.isPresent()) {
            model.addAttribute("action", action.get());
            logger.info("Exiting actionEdit (GET) method with action found");
            return "actions/action-edit";
        }
        logger.warn("Action with ID: {} not found", id);
        logger.info("Exiting actionEdit (GET) method with redirect");
        return "redirect:/admin/actions";
    }

    @PostMapping("/{id}")
    public String actionEdit(@PathVariable int id, @Valid @ModelAttribute Action action,
                             @RequestParam(required = false) MultipartFile file, BindingResult bindingResult,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        logger.info("Entering actionEdit (POST) method with ID: {}", id);
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "actions/action-edit";
        }
        try {
            Action existingAction = actionService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Action not found with id " + id));

            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                existingAction.setMainImage(fileName);
                logger.info("File uploaded successfully with name: {}", fileName);
            }

            if(additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                existingAction.getImages().clear();
                existingAction.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for action with id: {}", id);
            }

            existingAction.setName(action.getName());
            existingAction.setDescription(action.getDescription());
            existingAction.setLink(action.getLink());
            existingAction.setDescriptionSEO(action.getDescriptionSEO());
            existingAction.setKeywordsSEO(action.getKeywordsSEO());
            existingAction.setTitleSEO(action.getTitleSEO());

            actionService.save(existingAction);
            logger.info("Action updated successfully with ID: {}", existingAction.getId());
        } catch (IOException e) {
            logger.error("Error uploading file", e);
        }
        logger.info("Exiting actionEdit (POST) method");
        return "redirect:/admin/actions";
    }


    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> actionChangeStatus(@PathVariable long id) {
        logger.info("Entering actionChangeStatus method with ID: {}", id);
        Optional<Action> actionOptional = actionService.findById(id);
        if (actionOptional.isPresent()) {
            Action action = actionOptional.get();
            action.setNotActive(!action.isNotActive());
            actionService.save(action);
            logger.info("Action status changed successfully for ID: {}", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Action with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}")
    public String actionDelete(@PathVariable Long id){
        logger.info("Entering actionDelete method for ID: {}", id);

        try {
            Optional<Action> action = actionService.findById(id);
            action.ifPresent(actionService::delete);
            logger.info("Deleted action with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete action with ID: {}", id);
        }

        logger.info("Exiting actionDelete method");
        return "redirect:/admin/actions";
    }
}
