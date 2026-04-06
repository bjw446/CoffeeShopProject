package com.example.coffee_shop_project.infra.kafka.consumer;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.payment.dto.PaymentSuccessEvent;
import com.example.coffee_shop_project.infra.kafka.exception.KafkaCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${external.data-platform.url}")
    private String dataPlatformUrl;

    @KafkaListener(topics = "payment-events", groupId = "payment-group")
    public void consumePayment(String message, Acknowledgment acknowledgment) {
        try {
            PaymentSuccessEvent event = objectMapper.readValue(message, PaymentSuccessEvent.class);

            log.info("결제 이벤트 수신 - orderId={}, amount={}",
                    event.getOrderId(), event.getTotalAmount());

            sendToDataPlatform(event);

            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Payment consume 실패 message={}", message, e);
            throw new KafkaCustomException(ErrorStatus.KAFKA_CONSUME_ERROR);
        }
    }

    private void sendToDataPlatform(PaymentSuccessEvent event) {
        try {
            String url = dataPlatformUrl;

            Map<String, Object> request = Map.of(
                    "userId", event.getUserId(),
                    "orderId", event.getOrderId(),
                    "amount", event.getTotalAmount()
            );

            restTemplate.postForEntity(url, request, Void.class);

            log.info("데이터 플랫폼 전송 성공 orderId={}", event.getOrderId());
        } catch (Exception e) {
            log.error("데이터 플랫폼 전송 실패 orderId{}", event.getOrderId(), e);

            throw new KafkaCustomException(ErrorStatus.EXTERNAL_API_ERROR);
        }
    }
}
