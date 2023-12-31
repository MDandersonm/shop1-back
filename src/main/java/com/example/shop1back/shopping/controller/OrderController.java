package com.example.shop1back.shopping.controller;

import com.example.shop1back.shopping.entity.OrderInfo;
import com.example.shop1back.shopping.service.OrderService;
import com.example.shop1back.shopping.service.response.OrderListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    final private OrderService orderService;

    @PostMapping("/onlyuser/save")
    public String saveOrder(@RequestBody Map<String, Object> orderInfo) {
        log.info("saveOrder!!");
        orderService.saveOrder(orderInfo);
        return "Order saved successfully!";
    }

    @GetMapping("/onlyuser/list")
    public List<OrderListResponse> getAllOrders() {
        log.info("get all Order!!");
        return orderService.getAllOrders();
    }
}
