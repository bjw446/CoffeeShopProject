package com.example.coffee_shop_project.infra.kafka.consumer;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menusales.service.MenuSalesService;
import com.example.coffee_shop_project.domain.order.dto.OrderCreatedEvent;
import com.example.coffee_shop_project.infra.kafka.exception.KafkaCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final MenuSalesService menuSalesService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "menu-sales-group", concurrency = "1")
    @Transactional
    public void consumeOrder(String message, Acknowledgment acknowledgment) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);

            menuSalesService.updateSales(event);

            acknowledgment.acknowledge();

            log.info("Kafka consume 성공 - orderId={}, message={}", event.getOrderId(), message);

        } catch (Exception e) {
            log.error("Kafka consume 실패 message={}", message, e);
            throw new KafkaCustomException(ErrorStatus.KAFKA_CONSUME_ERROR);
        }
    }
}
