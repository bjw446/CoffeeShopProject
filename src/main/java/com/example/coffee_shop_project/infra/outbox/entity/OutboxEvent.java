package com.example.coffee_shop_project.infra.outbox.entity;

import com.example.coffee_shop_project.common.entity.CreatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox_events")
public class OutboxEvent extends CreatableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;

    private Long aggregateId;

    private String eventType;

    @Column(unique = true)
    private String eventKey;

    @Lob
    private String payload;

    private String status;

    private int retryCount;

    private LocalDateTime publishedAt;

    @Builder
    public OutboxEvent(String aggregateType, Long aggregateId, String eventType, String eventKey, String payload, String status) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.eventKey = eventKey;
        this.payload = payload;
        this.status = status;
    }

    public void markPublished() {
        this.status = "PUBLISHED";
        this.publishedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = "FAILED";
    }

    public void increaseRetryCount() {
        this.retryCount++;
    }
}
