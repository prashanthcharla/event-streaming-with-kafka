package com.producer.orders.service;

import com.producer.orders.constant.AppConstants;
import com.producer.orders.domain.Order;
import com.producer.orders.request.OrderRequest;
import com.producer.orders.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<Integer, Integer> orderKafkaTemplate;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order(
                Double.valueOf(Math.random() * 100).intValue(),
                orderRequest.getCustomerName(),
                orderRequest.getItems(),
                orderRequest.getDeliveryAddress(),
                AppConstants.WAREHOUSE_ADDRESS,
                LocalDate.now().plusDays(10)
        );

        System.out.println(order.orderId());

        orderKafkaTemplate.send(AppConstants.ORDERS_TOPIC, order.orderId(), order.orderId());

        return new OrderResponse(
                order.orderId(),
                order.customerName(),
                order.orderedItems(),
                order.deliveryAddress(),
                order.estimatedDeliveryDate()
        );
    }
}
