package com.example.shop1back.user.controller;

import com.example.shop1back.user.controller.form.ProductRegisterForm;
import com.example.shop1back.user.entity.Product;
import com.example.shop1back.user.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping(value = "/register",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String saveProduct(@RequestPart(value="image") MultipartFile image,
                              @RequestPart(value="product") ProductRegisterForm productRegisterForm) {
        log.info("product register! ");
        return productService.saveProduct( image,productRegisterForm);
    }
}