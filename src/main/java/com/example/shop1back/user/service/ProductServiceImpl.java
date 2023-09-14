package com.example.shop1back.user.service;

import com.example.shop1back.user.controller.form.ProductRegisterForm;
import com.example.shop1back.user.entity.Product;
import com.example.shop1back.user.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final String UPLOAD_DIR = "../shop1-front/public/images/product/"; // 이미지 저장 경로
    @Override
    public String saveProduct(MultipartFile image, ProductRegisterForm productRegisterForm) {
        Product product = new Product();
        product.setName(productRegisterForm.getName());
        product.setBrand(productRegisterForm.getBrand());
        product.setPrice(productRegisterForm.getPrice());
        try {
            // 이미지 처리
            if (image != null && !image.isEmpty()) {
                String originalFilename = image.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                // UUID와 현재 타임스탬프를 사용하여 파일명 생성
                String newFileName = UUID.randomUUID().toString() + System.currentTimeMillis() + fileExtension;

                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOAD_DIR + newFileName);
                Files.write(path, bytes);

                product.setImage(newFileName); // 이미지 URL을 Product 객체에 설정
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "이미지 저장 실패";
        }

        productRepository.save(product);
        return "상품등록 성공";
    }
}