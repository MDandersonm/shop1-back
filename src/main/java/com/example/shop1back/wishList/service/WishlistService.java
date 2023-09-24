package com.example.shop1back.wishList.service;

import com.example.shop1back.product.service.response.ProductListResponse;
import com.example.shop1back.wishList.repository.response.WishlistResponse;

import java.util.List;

public interface WishlistService {
    public WishlistResponse toggleWishlist(Long userId, Long productId);

    List<ProductListResponse> wishList(Long userId);

    boolean isWishlist(Long userId, Long productId);
}
