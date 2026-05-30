package com.producer.orders.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse (Integer orderId, String customerName, List<String> orderedItems, String deliveryAddress, LocalDate estimatedDeliveryDate) {
}
