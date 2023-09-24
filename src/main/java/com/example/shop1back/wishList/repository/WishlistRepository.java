package com.example.shop1back.wishList.repository;

import com.example.shop1back.wishList.entity.Wishlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(Long memberId, Long productId);


    List<Wishlist> findByUserIdOrderByProductIdDesc(Long userId);

    List<Wishlist> findByProductId(Long productId);
}
