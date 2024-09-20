package com.CMS.kinoCMS.controllers.Admin;

import com.CMS.kinoCMS.models.EmailTemplate;
import com.CMS.kinoCMS.repositories.EmailTemplateRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin/email-sending/email-templates")
public class EmailTemplateController {
    private static final Logger logger = LogManager.getLogger(EmailTemplateController.class);

    private final EmailTemplateRepository emailTemplateRepository;

    @Autowired
    public EmailTemplateController(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @GetMapping
    public String listTemplates(Model model) {
        model.addAttribute("templates", emailTemplateRepository.findAll());
        return "email-templates/list";
    }

    @GetMapping("/add")
    public String addTemplateForm(Model model) {
        model.addAttribute("template", new EmailTemplate());
        return "email-templates/add";
    }

    @PostMapping("/add")
    public String addTemplate(@Valid @ModelAttribute EmailTemplate template, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in addTemplate: {}", bindingResult.getAllErrors());
            return "email-templates/add";
        }
        emailTemplateRepository.save(template);
        return "redirect:/admin/email-sending/email-templates";
    }

    @GetMapping("/edit/{id}")
    public String editTemplateForm(@PathVariable Long id, Model model) {
        model.addAttribute("template", emailTemplateRepository.findById(id).orElseThrow(() -> {
            logger.error("Invalid template Id: {}", id);
            return new IllegalArgumentException("Invalid template Id:" + id);
        }));
        return "email-templates/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTemplate(@PathVariable Long id, @Valid @ModelAttribute EmailTemplate template, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in editTemplate: {}", bindingResult.getAllErrors());
            return "email-templates/edit";
        }
        template.setId(id);
        emailTemplateRepository.save(template);
        return "redirect:/admin/email-sending/email-templates";
    }

    @PostMapping("/delete/{id}")
    public String deleteTemplate(@PathVariable Long id) {
        emailTemplateRepository.deleteById(id);
        return "redirect:/admin/email-sending/email-templates";
    }
}
