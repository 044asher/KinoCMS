package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Action;
import com.CMS.kinoCMS.services.ActionService;
import com.CMS.kinoCMS.services.FileUploadService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
                            @RequestParam(required = false) MultipartFile file) {
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
                             @RequestParam(required = false) MultipartFile file, BindingResult bindingResult) {
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
                action.setMainImage(fileName);
                logger.info("File uploaded successfully with name: {}", fileName);
            }
            existingAction.setName(action.getName());
            existingAction.setDescription(action.getDescription());
            existingAction.setLink(action.getLink());
            existingAction.setDescriptionSEO(action.getDescriptionSEO());
            existingAction.setKeywordsSEO(action.getKeywordsSEO());
            existingAction.setTitleSEO(action.getTitleSEO());
            existingAction.setKeywordsSEO(action.getKeywordsSEO());
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
}
