package com.CMS.kinoCMS.admin.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class EmailStatisticsService {
    private final AtomicInteger emailsSent = new AtomicInteger(0);

    public void incrementEmailsSent() {
        log.info("Start EmailStatisticsService - incrementEmailsSent");
        int newCount = emailsSent.incrementAndGet();
        log.info("Successfully executed EmailStatisticsService - incrementEmailsSent. New count: {}", newCount);
    }

    public int getEmailsSent() {
        log.info("Start EmailStatisticsService - getEmailsSent");
        int currentCount = emailsSent.get();
        log.info("Successfully executed EmailStatisticsService - getEmailsSent. Current count: {}", currentCount);
        return currentCount;
    }

    public void resetEmailsSent() {
        log.info("Start EmailStatisticsService - resetEmailsSent");
        emailsSent.set(0);
        log.info("Successfully executed EmailStatisticsService - resetEmailsSent. Emails sent count has been reset.");
    }
}