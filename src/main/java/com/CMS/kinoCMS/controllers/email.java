package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.EmailTemplateRepository;
import com.CMS.kinoCMS.services.MailSender;
import com.CMS.kinoCMS.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/email-sending")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class email {
    private final UserService userService;
    private final MailSender mailSender;
    private final EmailTemplateRepository emailTemplateRepository;

    @Autowired
    public email(UserService userService, MailSender mailSender, EmailTemplateRepository emailTemplateRepository) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @GetMapping()
    public String emailSendingMain(Model model) {
        model.addAttribute("templates", emailTemplateRepository.findAll());
        return "emails/email";
    }

    @PostMapping("/all")
    public String mailSendingToAll(@RequestParam String subject, @RequestParam String message) {
        List<User> users = userService.findAllUsers();
        for (User user : users) {
            mailSender.sendHtmlEmail(user.getEmail(), subject, message);
        }
        return "redirect:/admin/email-sending";
    }

    @GetMapping("/select")
    public String emailSendingSelect(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("templates", emailTemplateRepository.findAll());
        return "emails/select";
    }

    @PostMapping("/select")
    public String mailSendingToSelected(
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam(required = false) List<Long> mailingUsers) {

        if (mailingUsers != null && !mailingUsers.isEmpty()) {
            for (Long userId : mailingUsers) {
                userService.findUserById(userId).ifPresent(user ->
                        mailSender.sendHtmlEmail(user.getEmail(), subject, message)
                );
            }
        }
        return "redirect:/admin/email-sending/select";
    }




}
