package com.example.coffee_shop_project.infra.kafka.consumer;

import com.example.coffee_shop_project.domain.menusales.service.MenuSalesService;
import com.example.coffee_shop_project.domain.order.dto.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final MenuSalesService menuSalesService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "menu-sales-group", concurrency = "1")
    public void consume(String message) throws Exception {
        OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);

        menuSalesService.updateSales(event);
    }
}
