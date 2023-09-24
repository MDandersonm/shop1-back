package com.example.shop1back.wishList.service;

import com.example.shop1back.product.entity.Product;
import com.example.shop1back.product.repository.ProductRepository;
import com.example.shop1back.product.service.response.ProductListResponse;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import com.example.shop1back.wishList.entity.Wishlist;
import com.example.shop1back.wishList.repository.WishlistRepository;
import com.example.shop1back.wishList.repository.response.WishlistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistResponse toggleWishlist(Long userId, Long productId) {

        Optional<Wishlist> existingWishlist = wishlistRepository.findByUserIdAndProductId(userId, productId);
        // 이미 있는 경우, 삭제하고 null 또는 적절한 응답 반환
        if (existingWishlist.isPresent()) {
            wishlistRepository.delete(existingWishlist.get());
            return null;
        }

//        if (existingWishlist.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 관심상품에 추가된 상품입니다.");
//        }


        // User와 Product 엔터티 참조 가져오기
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 새로운 Wishlist 생성 및 설정
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        WishlistResponse response = new WishlistResponse();
        response.setId(savedWishlist.getId());
        response.setUserId(userId);
        response.setProductId(productId);
        response.setProductName(product.getName());
        response.setProductBrand(product.getBrand());
        response.setProductPrice(product.getPrice());
        response.setProductImage(product.getImage());

        return response;
    }

    @Transactional
    //LazyInitializationException은 Hibernate에서 객체의 지연 로딩을 사용할 때 해당 객체를 로드하는 세션이 닫혀 있으면 발생
    //연 로딩된 관계에 액세스하는 동안 Hibernate 세션을 열어둠
    // @Transactional을 사용하면, wishList 메서드의 실행 동안 Hibernate 세션은 열린 상태로 유지, 지연 로딩된 객체에 접근할 때 세션이 여전히 활성 상태이므로 위의 예외가 발생하지 않음.
    @Override
    public List<ProductListResponse> wishList(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserIdOrderByProductIdDesc(userId);

        // Wishlist 엔터티 리스트를 ProductListResponse DTO 리스트로 변환
        return wishlists.stream()
                .map(wishlist -> wishlist.getProduct())  // Product 객체로 매핑
                .map(this::convertToProductListResponse) // Product 객체를 DTO로 변환
                .collect(Collectors.toList());

    }

    @Override
    public boolean isWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId,productId);
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
}
