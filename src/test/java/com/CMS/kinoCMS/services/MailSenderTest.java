package com.CMS.kinoCMS.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class MailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailSender mailService;

    private final String username = "test@example.com";

    @Test
    public void shouldSendSimpleEmail() {
        String emailTo = "test@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        Mockito.doNothing().when(mailSender).send(Mockito.any(SimpleMailMessage.class));

        mailService.send(emailTo, subject, message);

        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
    }
}
