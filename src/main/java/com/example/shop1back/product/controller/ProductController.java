package com.example.shop1back.product.controller;

import com.example.shop1back.product.controller.form.ProductRegisterForm;
import com.example.shop1back.product.service.ProductService;
import com.example.shop1back.product.service.response.ProductDetailResponse;
import com.example.shop1back.product.service.response.ProductListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/onlyadmin/register",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String saveProduct(@RequestPart(value = "image") MultipartFile image,
                              @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
                              @RequestPart(value = "product") ProductRegisterForm productRegisterForm) {
        log.info("product register! ");
        return productService.saveProduct(image, detailImages, productRegisterForm);
    }

    @GetMapping(path = "/list")
    public List<ProductListResponse> productList() {
        log.info("product list! ");
        return productService.productList();
    }

    @GetMapping("/detail/{productId}")
    public ProductDetailResponse productRead(@PathVariable("productId") Long productId) {
        log.info("product Read()");
        return productService.read(productId);
    }
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/onlyadmin/update/{productId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String updateProduct(@RequestPart(value = "image", required = false) MultipartFile image,
                                @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
                                @RequestPart(value = "product") ProductRegisterForm productRegisterForm,
                                @PathVariable("productId") Long productId) {
        log.info("product update! ");
        return productService.updateProduct(productId, image, detailImages, productRegisterForm);
    }
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/onlyadmin/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.info("product 삭제 컨트롤러");
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }


}