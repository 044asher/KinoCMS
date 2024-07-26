package com.CMS.kinoCMS.admin.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class EmailStatisticsService {
    private final AtomicInteger emailsSent = new AtomicInteger(0);

    public void incrementEmailsSent() {
        int newCount = emailsSent.incrementAndGet();
        log.info("Incremented emails sent count: {}", newCount);
    }

    public int getEmailsSent() {
        int currentCount = emailsSent.get();
        log.info("Current emails sent count: {}", currentCount);
        return currentCount;
    }

    public void resetEmailsSent() {
        emailsSent.set(0);
        log.info("Emails sent count has been reset");
    }
}
