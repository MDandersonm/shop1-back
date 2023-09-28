package com.example.shop1back.shopping.service;

import com.example.shop1back.shopping.entity.OrderInfo;
import com.example.shop1back.shopping.service.response.OrderListResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    void saveOrder(Map<String, Object> orderInfo);

    List<OrderListResponse> getAllOrders();
}
