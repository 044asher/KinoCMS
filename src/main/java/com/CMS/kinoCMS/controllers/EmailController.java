package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.EmailTemplateRepository;
import com.CMS.kinoCMS.services.EmailStatisticsService;
import com.CMS.kinoCMS.services.MailSender;
import com.CMS.kinoCMS.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class EmailController {
    private static final Logger logger = LogManager.getLogger(EmailController.class);

    private final UserService userService;
    private final MailSender mailSender;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailStatisticsService emailStatisticsService;

    @Autowired
    public EmailController(UserService userService, MailSender mailSender, EmailTemplateRepository emailTemplateRepository, EmailStatisticsService emailStatisticsService) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailStatisticsService = emailStatisticsService;
    }

    @GetMapping()
    public String emailSendingMain(Model model) {
        logger.info("Entering emailSendingMain method");
        model.addAttribute("templates", emailTemplateRepository.findAll());
        model.addAttribute("totalEmailsSent", emailStatisticsService.getEmailsSent());
        logger.info("Exiting emailSendingMain method");
        return "emails/email";
    }

    @PostMapping("/all")
    public @ResponseBody CompletableFuture<ResponseEntity<Map<String, Object>>> mailSendingToAll(@RequestParam String subject, @RequestParam String message) {
        logger.info("Entering mailSendingToAll method with subject: {} and message: {}", subject, message);
        List<User> users = userService.findAllUsers();
        emailStatisticsService.resetEmailsSent();
        AtomicInteger counter = new AtomicInteger();

        return CompletableFuture.runAsync(() -> {
            for (User user : users) {
                logger.info("Sending email to: {}", user.getEmail());
                mailSender.sendHtmlEmail(user.getEmail(), subject, message);
                emailStatisticsService.incrementEmailsSent();
                counter.incrementAndGet();
            }
        }).thenApply(v -> {
            Map<String, Object> response = new HashMap<>();
            response.put("emailsSent", counter.get());
            response.put("totalUsers", users.size());
            logger.info("Emails sent: {}, Total users: {}", counter.get(), users.size());
            logger.info("Exiting mailSendingToAll method");
            return ResponseEntity.ok(response);
        });
    }

    @GetMapping("/progress")
    public @ResponseBody ResponseEntity<Map<String, Integer>> getProgress() {
        logger.info("Entering getProgress method");
        Map<String, Integer> response = new HashMap<>();
        response.put("emailsSent", emailStatisticsService.getEmailsSent());
        response.put("totalUsers", userService.findAllUsers().size());
        logger.info("Emails sent: {}, Total users: {}", response.get("emailsSent"), response.get("totalUsers"));
        logger.info("Exiting getProgress method");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/select")
    public String emailSendingSelect(Model model) {
        logger.info("Entering emailSendingSelect method");
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("templates", emailTemplateRepository.findAll());
        logger.info("Exiting emailSendingSelect method");
        return "emails/select";
    }

    @PostMapping("/select")
    public String mailSendingToSelected(
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam(required = false) List<Long> mailingUsers) {
        logger.info("Entering mailSendingToSelected method with subject: {} and message: {}", subject, message);
        if (mailingUsers != null && !mailingUsers.isEmpty()) {
            for (Long userId : mailingUsers) {
                userService.findUserById(userId).ifPresent(user -> {
                    logger.info("Sending email to: {}", user.getEmail());
                    mailSender.sendHtmlEmail(user.getEmail(), subject, message);
                });
            }
        }
        logger.info("Exiting mailSendingToSelected method");
        return "redirect:/admin/email-sending/select";
    }
}
