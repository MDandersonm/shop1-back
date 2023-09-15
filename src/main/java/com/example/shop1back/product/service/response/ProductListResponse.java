package com.example.shop1back.product.service.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
}
