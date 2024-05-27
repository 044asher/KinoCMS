package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.EmailTemplateRepository;
import com.CMS.kinoCMS.services.EmailStatisticsService;
import com.CMS.kinoCMS.services.MailSender;
import com.CMS.kinoCMS.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/admin/email-sending")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class email {
    private final UserService userService;
    private final MailSender mailSender;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailStatisticsService emailStatisticsService;

    @Autowired
    public email(UserService userService, MailSender mailSender, EmailTemplateRepository emailTemplateRepository, EmailStatisticsService emailStatisticsService) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailStatisticsService = emailStatisticsService;
    }

    @GetMapping()
    public String emailSendingMain(Model model) {
        model.addAttribute("templates", emailTemplateRepository.findAll());
        model.addAttribute("totalEmailsSent", emailStatisticsService.getTotalEmailsSent());
        return "emails/email";
    }

    @PostMapping("/all")
    public @ResponseBody CompletableFuture<ResponseEntity<Map<String, Object>>> mailSendingToAll(@RequestParam String subject, @RequestParam String message) {
        List<User> users = userService.findAllUsers();
        AtomicInteger counter = new AtomicInteger();

        return CompletableFuture.runAsync(() -> {
            for (User user : users) {
                mailSender.sendHtmlEmail(user.getEmail(), subject, message);
                emailStatisticsService.incrementEmailsSent();
                counter.incrementAndGet();
                // Здесь вы можете сохранить прогресс в общей базе данных или в памяти
            }
        }).thenApply(v -> {
            Map<String, Object> response = new HashMap<>();
            response.put("emailsSent", counter.get());
            response.put("totalUsers", users.size());
            return ResponseEntity.ok(response);
        });
    }
    @GetMapping("/progress")
    public @ResponseBody int getProgress() {
        return emailStatisticsService.getEmailsSentInCurrentSession();
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
