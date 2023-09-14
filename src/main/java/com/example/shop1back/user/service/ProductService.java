package com.example.shop1back.user.service;


import com.example.shop1back.user.controller.form.ProductRegisterForm;
import com.example.shop1back.user.entity.Product;
import com.example.shop1back.user.repository.ProductRepository;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    String saveProduct(MultipartFile image, ProductRegisterForm productRegisterForm);

}
