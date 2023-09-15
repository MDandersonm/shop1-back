package com.example.shop1back.product.service;


import com.example.shop1back.product.controller.form.ProductRegisterForm;
import com.example.shop1back.product.service.response.ProductDetailResponse;
import com.example.shop1back.product.service.response.ProductListResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    String saveProduct(MultipartFile image,List<MultipartFile> detailImages, ProductRegisterForm productRegisterForm);
    List<ProductListResponse> productList();
    ProductDetailResponse read(Long productId);
}
