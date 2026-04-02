package com.example.coffee_shop_project.domain.orderitems.service;

import com.example.coffee_shop_project.domain.orderitems.repository.OrderItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemsService {
    private final OrderItemsRepository orderItemsRepository;

    @Transactional(readOnly = true)
    public List<String> getTop3Menus() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);

        List<Object[]> results = orderItemsRepository.findTopMenus(start, PageRequest.of(0, 3));

        List<String> menus = new ArrayList<>();

        for (Object[] r : results) {
            menus.add((String) r[0]);
        }

        return menus;
    }
}
