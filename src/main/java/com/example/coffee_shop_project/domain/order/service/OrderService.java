package com.example.coffee_shop_project.domain.order.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderCreatedEvent;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.repository.OrderRepository;
import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import com.example.coffee_shop_project.domain.orderitems.entity.OrderItems;
import com.example.coffee_shop_project.domain.orderitems.exception.OrderItemsException;
import com.example.coffee_shop_project.domain.orderitems.repository.OrderItemsRepository;
import com.example.coffee_shop_project.domain.payment.service.PaymentService;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import com.example.coffee_shop_project.infra.outbox.entity.OutboxEvent;
import com.example.coffee_shop_project.infra.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;

    public OrderResponse createOrder(CreateOrderRequest request) throws JsonProcessingException {
        User user = null;

        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new UserException(ErrorStatus.USER_NOT_FOUND)
            );
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new OrderItemsException(ErrorStatus.ORDER_ITEMS_NOT_FOUND);
        }

        List<OrderItems> items = convertToOrderItems(request.getItems());

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

        List<CreateOrderItems> payload = savedOrderItems.stream()
                        .map(i -> new CreateOrderItems(
                                i.getMenuName(),
                                i.getPrice(),
                                i.getQuantity()

                        ))
                                .toList();

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                payload
        );

        outboxRepository.save(
                OutboxEvent.builder()
                        .aggregateType("ORDER")
                        .aggregateId(savedOrder.getId())
                        .eventType("ORDER_CREATED")
                        .eventKey(UUID.randomUUID().toString())
                        .payload(objectMapper.writeValueAsString(event))
                        .status("PENDING")
                        .build()
        );

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
        RLock lock = redissonClient.getLock("lock:order:orderNumber");

        try {
            boolean available = lock.tryLock(5, 3, TimeUnit.SECONDS);

            if (!available) {
                throw new OrderException(ErrorStatus.LOCK_ACQUISITION_FAILED);
            }

            Long maxOrderNumber = orderRepository.findMaxOrderNumber();
            return maxOrderNumber + 1;
        } catch (InterruptedException e) {
            throw new OrderException(ErrorStatus.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse findOneOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorStatus.ORDER_NOT_FOUND)
        );

        return OrderResponse.from(order);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorStatus.ORDER_NOT_FOUND)
        );

        order.cancelOrder();
    }

    public void cancelOrderByAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException(ErrorStatus.ORDER_NOT_FOUND)
        );

        if (order.getOrderStatus() == OrderStatus.PAID) {
            paymentService.cancelPayment(order.getId());
        }

        order.cancelOrderByAdmin();
    }
}
