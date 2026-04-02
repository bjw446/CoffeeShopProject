package com.example.coffee_shop_project.infra.outbox.scheduler;

import com.example.coffee_shop_project.infra.outbox.publisher.OutboxPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxPublisher outboxPublisher;

    @Scheduled(fixedDelay = 1000)
    public void publish() {
        outboxPublisher.publishPendingEvents();
    }
}
