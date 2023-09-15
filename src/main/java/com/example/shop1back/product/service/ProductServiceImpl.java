package com.example.shop1back.product.service;

import com.example.shop1back.product.controller.form.ProductRegisterForm;
import com.example.shop1back.product.entity.Product;
import com.example.shop1back.product.entity.ProductDetailImage;
import com.example.shop1back.product.repository.ProductRepository;
import com.example.shop1back.product.service.response.ProductDetailResponse;
import com.example.shop1back.product.service.response.ProductListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final String UPLOAD_DIR = "../shop1-front/public/images/product/"; // 이미지 저장 경로

    @Override
    public String saveProduct(MultipartFile image, List<MultipartFile> detailImages, ProductRegisterForm productRegisterForm) {
        Product product = new Product();
        product.setName(productRegisterForm.getName());
        product.setBrand(productRegisterForm.getBrand());
        product.setPrice(productRegisterForm.getPrice());
        try {
            // 썸네일이미지 처리
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
        if (detailImages != null) {
            try {
                // 상세 이미지 처리
                for (MultipartFile detailImage : detailImages) {
                    if (detailImage != null && !detailImage.isEmpty()) {
                        String originalFilename = detailImage.getOriginalFilename();
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                        String newFileName = UUID.randomUUID().toString() + System.currentTimeMillis() + fileExtension;

                        byte[] bytes = detailImage.getBytes();
                        Path path = Paths.get(UPLOAD_DIR + newFileName);
                        Files.write(path, bytes);

                        ProductDetailImage detailImageEntity = new ProductDetailImage();
                        detailImageEntity.setDetailImageUrl(newFileName);
                        detailImageEntity.setProduct(product); // 상품과 연결
                        product.getDetailImages().add(detailImageEntity); // 상품 상세 이미지 리스트에 추가
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "상세 이미지 저장 실패";
            }
        }

        productRepository.save(product);
        return "상품등록 성공";
    }

    @Override
    public List<ProductListResponse> productList() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        for(Product product : products) {
            System.out.println("Product ID: " + product.getId());
            System.out.println("Product Name: " + product.getName());
        }
        log.info("product list services! ");
        // Product 엔터티 리스트를 ProductListResponse DTO 리스트로 변환
        return products.stream()
                .map(this::convertToProductListResponse)
                .collect(Collectors.toList());
    }



    private ProductListResponse convertToProductListResponse(Product product) {
        ProductListResponse response = new ProductListResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setImage(product.getImage());
        return response;
    }


    @Override
    public ProductDetailResponse read(Long productId) {
        Optional<Product> productOpt = productRepository.findByIdWithDetailImages(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found for ID: " + productId);
        }
        Product product = productOpt.get();
        List<ProductDetailImage> detailImages = product.getDetailImages() != null ? product.getDetailImages() : new ArrayList<>();
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getImage(),
                detailImages
        );
    }

}
