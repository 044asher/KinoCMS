package com.CMS.kinoCMS.admin.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailSender mailService;

    private String username = "test@example.com";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(mailService, "username", username); // Set the private field username
    }

    @Test
    public void testSendHtmlEmail_Success() {
        String emailTo = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendHtmlEmail(emailTo, subject, message);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendHtmlEmail_MessagingException() {
        String emailTo = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        doAnswer((Answer<Void>) _ -> {
            throw new MessagingException("Test Exception");
        }).when(mailSender).send(any(MimeMessage.class));

        mailService.sendHtmlEmail(emailTo, subject, message);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);

    }
}