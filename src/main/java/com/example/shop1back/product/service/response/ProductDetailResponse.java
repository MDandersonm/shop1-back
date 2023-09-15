package com.example.shop1back.product.service.response;

import com.example.shop1back.product.entity.ProductDetailImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
    private List<ProductDetailImage> detailImages;

}
