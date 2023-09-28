package com.example.shop1back.shopping.service.response;

import com.example.shop1back.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponse {
    private Long id;
    private User user;
    private Double totalPrice;
    private String name;
    private String postCode;
    private String address;
    private String detailAddress;
    private String phone;
    private LocalDateTime orderDate;
    private List<OrderProductResponse> orderProducts;


}
