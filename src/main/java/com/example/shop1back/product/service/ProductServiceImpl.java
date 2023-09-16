package com.example.shop1back.product.service;

import com.example.shop1back.product.controller.form.ProductRegisterForm;
import com.example.shop1back.product.entity.Product;
import com.example.shop1back.product.entity.ProductDetailImage;
import com.example.shop1back.product.repository.ProductDetailImageRepository;
import com.example.shop1back.product.repository.ProductRepository;
import com.example.shop1back.product.service.response.ProductDetailResponse;
import com.example.shop1back.product.service.response.ProductListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailImageRepository productDetailImageRepository;
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
        for (Product product : products) {
            System.out.println("Product ID: " + product.getId());
            System.out.println("Product Name: " + product.getName());
        }
        log.info("product list services! ");
        // Product 엔터티 리스트를 ProductListResponse DTO 리스트로 변환
        return products.stream().map(this::convertToProductListResponse).collect(Collectors.toList());
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
        return new ProductDetailResponse(product.getId(), product.getName(), product.getBrand(), product.getPrice(), product.getImage(), detailImages);
    }

    @Override
    public String updateProduct(Long productId, MultipartFile image, List<MultipartFile> detailImages, ProductRegisterForm productRegisterForm) {
        // 상품 정보 찾기
        Optional<Product> existingProductOpt = productRepository.findByIdWithDetailImages(productId);
        if (!existingProductOpt.isPresent()) {
            return "해당 상품을 찾을 수 없습니다.";
        }
        Product product = existingProductOpt.get();
        product.setName(productRegisterForm.getName());
        product.setBrand(productRegisterForm.getBrand());
        product.setPrice(productRegisterForm.getPrice());

        try {
            // 썸네일 이미지 처리
            if (image != null && !image.isEmpty()) {
                // 기존 이미지 삭제 로직 (옵션)
                String existingImageUrl = product.getImage();
                if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                    Path existingImagePath = Paths.get(UPLOAD_DIR + existingImageUrl);
                    Files.deleteIfExists(existingImagePath);
                }

                String originalFilename = image.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + System.currentTimeMillis() + fileExtension;

                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOAD_DIR + newFileName);
                Files.write(path, bytes);

                product.setImage(newFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "이미지 저장 실패";
        }


        // 새로운 상세 이미지 추가
        if (detailImages != null) {
            // 기존의 상세 이미지 삭제
            List<ProductDetailImage> existingDetailImages = new ArrayList<>(product.getDetailImages());
            if (!existingDetailImages.isEmpty()) {
                for (ProductDetailImage detailImageEntity : existingDetailImages) {
                    String existingDetailImageUrl = detailImageEntity.getDetailImageUrl();

                    // 서버의 파일 시스템에서 이미지 파일 삭제
                    if (existingDetailImageUrl != null && !existingDetailImageUrl.isEmpty()) {
                        Path existingDetailImagePath = Paths.get(UPLOAD_DIR + existingDetailImageUrl);
                        try {
                            Files.deleteIfExists(existingDetailImagePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //상세이미지 엔티티 명시적 삭제
                    productDetailImageRepository.delete(detailImageEntity);
                }
                System.out.println("product.getDetailImages().toString()" + product.getDetailImages().toString());
                //clear해서 product를 덮어씌운다해도  productDetailImage db에 있던 데이터가 삭제되진 않는다
                product.getDetailImages().clear();
                System.out.println("후product.getDetailImages().toString()" + product.getDetailImages().toString());
            }


            try {
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
                        detailImageEntity.setProduct(product);
                        product.getDetailImages().add(detailImageEntity);
                        System.out.println("입력product.getDetailImages().toString()" + product.getDetailImages().toString());

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "상세 이미지 저장 실패";
            }
        }
        System.out.println("최종 세이브 전product.getDetailImages().toString()" + product.getDetailImages().toString());
        productRepository.save(product);
        return "상품 수정 성공";
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }


}