package com.example.shop1back.wishList.controller;

import com.example.shop1back.product.service.response.ProductListResponse;
import com.example.shop1back.wishList.entity.Wishlist;
import com.example.shop1back.wishList.repository.response.WishlistResponse;
import com.example.shop1back.wishList.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {


    private final WishlistService wishlistService;

    @PostMapping("/useronly/toggle/{userId}/{productId}")
    public ResponseEntity<?> toggleToWishlist(@PathVariable Long userId, @PathVariable Long productId) {

        WishlistResponse toggleWishlist = wishlistService.toggleWishlist(userId, productId);

        if (toggleWishlist == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(toggleWishlist, HttpStatus.CREATED);
        }
    }
    @GetMapping(path = "/useronly/list/{userId}")
    public List<ProductListResponse> wishList(@PathVariable Long userId) {
        log.info("wishlist list! ");
        return wishlistService.wishList(userId);
    }
    @GetMapping("/useronly/check/{userId}/{productId}")
    public ResponseEntity<Boolean> isWishList(@PathVariable Long userId, @PathVariable Long productId) {
        boolean isWishlist = wishlistService.isWishlist(userId, productId);
        return ResponseEntity.ok(isWishlist);
    }
}
