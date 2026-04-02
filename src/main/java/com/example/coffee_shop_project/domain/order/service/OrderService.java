package com.example.coffee_shop_project.domain.order.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.repository.OrderRepository;
import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import com.example.coffee_shop_project.domain.orderitems.entity.OrderItems;
import com.example.coffee_shop_project.domain.orderitems.exception.OrderItemsException;
import com.example.coffee_shop_project.domain.orderitems.repository.OrderItemsRepository;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserRepository userRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = null;

        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new UserException(ErrorStatus.USER_NOT_FOUND)
            );
        }

        List<OrderItems> items = convertToOrderItems(request.getItems());

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new OrderItemsException(ErrorStatus.ORDER_ITEMS_NOT_FOUND);
        }

        Long orderNumber = generateOrderNumber();

        Long totalAmount = calculateTotalAmount(items);

        Order order = Order.builder()
                .user(user)
                .orderNumber(orderNumber)
                .totalAmount(totalAmount)
                .orderType(request.getOrderType())
                .orderStatus(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        items.forEach(item -> item.connectOrder(savedOrder));

        List<OrderItems> savedOrderItems = orderItemsRepository.saveAll(items);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderItems> convertToOrderItems(List<CreateOrderItems> items) {
        return items.stream()
                .map(i -> OrderItems.builder()
                        .menuName(i.getMenuName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build())
                .toList();
    }

    private Long calculateTotalAmount(List<OrderItems> items) {
        return items.stream()
                .mapToLong(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    private Long generateOrderNumber() {
        Long maxOrderNumber = orderRepository.findMaxOrderNumber();
        return maxOrderNumber + 1;
    }

    @Transactional(readOnly = true)
    public OrderResponse findOneOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorStatus.ORDER_NOT_FOUND)
        );

        return OrderResponse.from(order);
    }
}
