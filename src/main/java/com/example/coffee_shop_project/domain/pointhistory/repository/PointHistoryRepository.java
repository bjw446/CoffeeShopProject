package com.example.coffee_shop_project.domain.pointhistory.repository;

import com.example.coffee_shop_project.domain.pointhistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
