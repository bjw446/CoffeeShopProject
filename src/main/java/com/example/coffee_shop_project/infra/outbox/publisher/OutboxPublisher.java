package com.example.coffee_shop_project.infra.outbox.publisher;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.infra.kafka.producer.KafkaEventProducer;
import com.example.coffee_shop_project.infra.outbox.entity.OutboxEvent;
import com.example.coffee_shop_project.infra.outbox.exception.EventCustomException;
import com.example.coffee_shop_project.infra.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaEventProducer kafkaEventProducer;

    private static final int MAX_RETRY = 3;

    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxRepository.findRetryableEvents(PageRequest.of(0, 100));

        for (OutboxEvent event : events) {
            if (event.getRetryCount() >= MAX_RETRY) {
                log.error("Dead Event 발생: {}", event.getId());
                continue;
            }
            try {
                String topic = switch (event.getEventType()) {
                    case "ORDER_CREATED" -> "order-events";
                    case "PAYMENT_SUCCESS" -> "payment-events";
                    default -> throw new EventCustomException(ErrorStatus.EVENT_TYPE_NOT_FOUND);
                };

                kafkaEventProducer.send(topic, event.getPayload());

                event.markPublished();
            } catch (Exception e) {
                event.increaseRetryCount();
                event.markFailed();
                log.error("Kafka publish 실패 - eventId={}, retryCount={}", event.getId(), event.getRetryCount(), e);
            }

        }
    }
}
