package com.producer.orders.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private String customerName;
    private List<String> items;
    private String deliveryAddress;
}
