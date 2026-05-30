package com.consumer.delivery_partner_B.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeliveryPartnerService {

    @KafkaListener(topics = "orders", groupId = "delivery-partner-group")
    public void startDelivery(Integer orderId) {
        System.out.println("Order ID: " + orderId);
    }
}
