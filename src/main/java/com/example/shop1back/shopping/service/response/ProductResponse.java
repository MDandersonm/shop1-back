package com.example.shop1back.shopping.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
    private LocalDateTime createdDate;
    private List<ProductDetailImageResponse> detailImages;
}