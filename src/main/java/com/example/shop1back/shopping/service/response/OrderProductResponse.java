package com.example.shop1back.shopping.service.response;

import com.example.shop1back.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
    private String size;
    private Double price;

}
