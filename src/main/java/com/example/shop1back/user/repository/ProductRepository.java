package com.example.shop1back.user.repository;

import com.example.shop1back.user.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}