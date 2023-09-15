package com.example.shop1back.product.repository;

import com.example.shop1back.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.detailImages WHERE p.id = :id")
    Optional<Product> findByIdWithDetailImages(Long id);


}