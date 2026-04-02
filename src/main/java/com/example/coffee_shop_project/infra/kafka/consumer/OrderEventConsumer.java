package com.example.coffee_shop_project.infra.kafka.consumer;

import com.example.coffee_shop_project.domain.menusales.service.MenuSalesService;
import com.example.coffee_shop_project.domain.order.dto.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void consume(String message) throws JsonProcessingException {
        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);

            menuSalesService.updateSales(event);
        } catch (Exception e) {
            log.error("Kafka consume 실패", e);
            throw e;
        }
    }
}
