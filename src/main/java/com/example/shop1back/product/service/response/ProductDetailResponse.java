package com.example.shop1back.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
}
