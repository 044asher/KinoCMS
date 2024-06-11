package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Action;
import com.CMS.kinoCMS.services.ActionService;
import com.CMS.kinoCMS.services.FileUploadService;
import jakarta.validation.Valid;
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
    private final ActionService actionService;
    private final FileUploadService fileUploadService;

    @Autowired
    public ActionController(ActionService actionService, FileUploadService fileUploadService) {
        this.actionService = actionService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String actions(Model model) {
        model.addAttribute("actions", actionService.findAll());
        return "actions/action-list";
    }

    @GetMapping("/add")
    public String actionAdd(Model model) {
        model.addAttribute("action", new Action());
        return "actions/action-add";
    }

    @PostMapping("/add")
    public String actionAdd(@Valid @ModelAttribute Action action,
                            BindingResult bindingResult,
                            @RequestParam(required = false) MultipartFile file) {
        if(bindingResult.hasErrors()) {
            return "actions/action-add";
        }
        try{
            if(file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                action.setMainImage(fileName);
            }
            action.setDateOfCreation(LocalDate.now());
            actionService.save(action);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/admin/actions";
    }

    @GetMapping("/{id}")
    public String actionEdit(@PathVariable int id, Model model) {
        Optional<Action> action = actionService.findById(id);
        if(action.isPresent()) {
            model.addAttribute("action", action.get());
            return "actions/action-edit";
        }
        return "redirect:/admin/actions";
    }
    @PostMapping("/{id}")
    public String actionEdit(@PathVariable int id, @Valid @ModelAttribute Action action,
                             @RequestParam(required = false) MultipartFile file, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "actions/action-edit";
        }
        try {
            Action existingAction = actionService.findById(id).orElseThrow(() -> new IllegalArgumentException("Action not found with id " + id));
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                action.setMainImage(fileName);
            }
            existingAction.setName(action.getName());
            existingAction.setDescription(action.getDescription());
            existingAction.setLink(action.getLink());

            existingAction.setDescriptionSEO(action.getDescriptionSEO());
            existingAction.setKeywordsSEO(action.getKeywordsSEO());
            existingAction.setTitleSEO(action.getTitleSEO());
            existingAction.setKeywordsSEO(action.getKeywordsSEO());
            actionService.save(existingAction);

        }catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/admin/actions";
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> actionChangeStatus(@PathVariable long id) {
        Optional<Action> actionOptional = actionService.findById(id);
        if (actionOptional.isPresent()) {
            Action action = actionOptional.get();
            action.setNotActive(!action.isNotActive());
            actionService.save(action);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.notFound().build();
    }
}
