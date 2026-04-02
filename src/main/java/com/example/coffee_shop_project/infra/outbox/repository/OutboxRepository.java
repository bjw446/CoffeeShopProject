package com.example.coffee_shop_project.infra.outbox.repository;

import com.example.coffee_shop_project.infra.outbox.entity.OutboxEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    @Query("SELECT o FROM OutboxEvent o WHERE o.status IN ('PENDING', 'FAILED')")
    List<OutboxEvent> findRetryableEvents(Pageable pageable);
}
