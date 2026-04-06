package com.example.coffee_shop_project.domain.order.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private final OrderService orderService;

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<CommonResponse<Void>> cancelOrderByAdmin(@PathVariable Long orderId) {

        orderService.cancelOrderByAdmin(orderId);

        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.UPDATE_SUCCESS, null));
    }
}
