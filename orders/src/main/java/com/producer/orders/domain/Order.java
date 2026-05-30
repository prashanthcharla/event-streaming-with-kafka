package com.producer.orders.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public record Order(Integer orderId, String customerName, List<String> orderedItems, String deliveryAddress,
                    String warehouseAddress, LocalDate estimatedDeliveryDate) {
}
