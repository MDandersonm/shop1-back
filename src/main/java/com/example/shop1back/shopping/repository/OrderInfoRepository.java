package com.example.shop1back.shopping.repository;

import com.example.shop1back.product.entity.Product;
import com.example.shop1back.shopping.entity.OrderInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    // OrderInfo와 orderProducts만 fetch
    @Query("SELECT DISTINCT o FROM OrderInfo o JOIN FETCH o.orderProducts op WHERE o.id = :orderId")
    Optional<OrderInfo> findByIdWithProducts(Long orderId);

    @Query("SELECT DISTINCT o FROM OrderInfo o JOIN FETCH o.orderProducts op")
    List<OrderInfo> findAllWithOrderProducts(Sort sort);

    // orderProduct에 대한 Product와 detailImages fetch
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.detailImages WHERE p IN :products")
    List<Product> findProductsWithDetailImages(Set<Product> products);
}
