package com.CMS.kinoCMS.controllers.Admin;

import com.CMS.kinoCMS.models.Action;
import com.CMS.kinoCMS.services.ActionService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/admin/actions")
public class ActionController {

    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
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
                            @RequestParam(required = false) MultipartFile file,
                            @RequestParam("additionalFiles") MultipartFile[] additionalFiles) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in actionAdd: {}", bindingResult.getAllErrors());
            return "actions/action-add";
        }
        try {
            actionService.saveAction(action, file, additionalFiles);
        } catch (IOException e) {
            log.error("Error while saving action: {}", e.getMessage(), e);
        }
        return "redirect:/admin/actions";
    }

    @GetMapping("/{id}")
    public String actionEdit(@PathVariable long id, Model model) {
        Optional<Action> action = actionService.findById(id);
        if (action.isPresent()) {
            model.addAttribute("action", action.get());
            return "actions/action-edit";
        }
        return "redirect:/admin/actions";
    }

    @PostMapping("/{id}")
    public String actionEdit(@PathVariable long id, @Valid @ModelAttribute Action action,
                             @RequestParam(required = false) MultipartFile file, BindingResult bindingResult,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation error in actionEdit: {}", bindingResult.getAllErrors());
            return "actions/action-edit";
        }
        try {
            actionService.updateAction(id, action, file, additionalFiles);
        } catch (IOException e) {
            log.error("Error while updating action: {}", e.getMessage(), e);
        }
        return "redirect:/admin/actions";
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> actionChangeStatus(@PathVariable long id) {
        try {
            actionService.changeActionStatus(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}")
    public String actionDelete(@PathVariable long id) {
        try {
            Optional<Action> action = actionService.findById(id);
            action.ifPresent(actionService::delete);
        } catch (Exception e) {
            log.error("Error while deleting action with ID {}: {}", id, e.getMessage(), e);
        }
        return "redirect:/admin/actions";
    }
}