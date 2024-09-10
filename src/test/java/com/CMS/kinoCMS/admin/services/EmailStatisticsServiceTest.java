package com.CMS.kinoCMS.admin.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailStatisticsServiceTest {

    private EmailStatisticsService emailStatisticsService;

    @BeforeEach
    void setUp() {
        emailStatisticsService = new EmailStatisticsService();
    }

    @Test
    void testIncrementEmailsSent() {
        emailStatisticsService.incrementEmailsSent();
        emailStatisticsService.incrementEmailsSent();

        assertEquals(2, emailStatisticsService.getEmailsSent());
    }

    @Test
    void testGetEmailsSent() {
        emailStatisticsService.incrementEmailsSent();
        emailStatisticsService.incrementEmailsSent();

        int count = emailStatisticsService.getEmailsSent();

        assertEquals(2, count);
    }

    @Test
    void testResetEmailsSent() {
        emailStatisticsService.incrementEmailsSent();
        emailStatisticsService.incrementEmailsSent();

        emailStatisticsService.resetEmailsSent();

        assertEquals(0, emailStatisticsService.getEmailsSent());
    }
}
