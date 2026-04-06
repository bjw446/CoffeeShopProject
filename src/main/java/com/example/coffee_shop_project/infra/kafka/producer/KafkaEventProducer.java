package com.example.coffee_shop_project.infra.kafka.producer;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.infra.kafka.exception.KafkaCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message).get();
        } catch (Exception e) {
            throw new KafkaCustomException(ErrorStatus.KAFKA_SEND_ERROR);
        }
    }
}
