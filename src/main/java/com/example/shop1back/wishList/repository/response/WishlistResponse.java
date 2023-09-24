package com.example.shop1back.wishList.repository.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WishlistResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String productBrand;
    private Double productPrice;
    private String productImage;
    // getter, setter 및 기타 필요한 메서드
}
