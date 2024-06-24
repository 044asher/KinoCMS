package com.CMS.kinoCMS.admin.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmailStatisticsService {
    private final AtomicInteger emailsSent = new AtomicInteger(0);

    public void incrementEmailsSent() {
        emailsSent.incrementAndGet();
    }

    public int getEmailsSent() {
        return emailsSent.get();
    }

    public void resetEmailsSent() {
        emailsSent.set(0);
    }
}
