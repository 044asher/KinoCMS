package com.CMS.kinoCMS.admin.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public MailSender(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendHtmlEmail(String emailTo, String subject, String message) {
        log.info("Start MailSender - sendHtmlEmail. Sending mail to: {}", emailTo);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(username);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error while sending formatted HTML message", e);
        }
        log.info("End MailSender - sendHtmlEmail. Successfully sent mail to: {}", emailTo);
    }
}
