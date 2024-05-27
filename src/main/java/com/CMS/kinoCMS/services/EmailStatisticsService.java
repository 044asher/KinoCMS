package com.CMS.kinoCMS.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmailStatisticsService {

    private AtomicInteger totalEmailsSent = new AtomicInteger(0);
    private AtomicInteger emailsSentInCurrentSession = new AtomicInteger(0);

    public int getTotalEmailsSent() {
        return totalEmailsSent.get();
    }

    public void incrementEmailsSent() {
        totalEmailsSent.incrementAndGet();
        emailsSentInCurrentSession.incrementAndGet();
    }

    public int getEmailsSentInCurrentSession() {
        return emailsSentInCurrentSession.get();
    }

    public void resetSessionCounter() {
        emailsSentInCurrentSession.set(0);
    }
}
