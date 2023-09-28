package com.example.shop1back.shopping.service;

import com.example.shop1back.product.entity.Product;
import com.example.shop1back.product.entity.ProductDetailImage;
import com.example.shop1back.product.repository.ProductRepository;
import com.example.shop1back.shopping.entity.OrderInfo;
import com.example.shop1back.shopping.entity.OrderProduct;
import com.example.shop1back.shopping.repository.OrderProductRepository;
import com.example.shop1back.shopping.repository.OrderInfoRepository;
import com.example.shop1back.shopping.service.response.OrderListResponse;
import com.example.shop1back.shopping.service.response.OrderProductResponse;
import com.example.shop1back.shopping.service.response.ProductDetailImageResponse;
import com.example.shop1back.shopping.service.response.ProductResponse;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final OrderInfoRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    @Override
    public void saveOrder(Map<String, Object> orderInfo) {
        OrderInfo order = new OrderInfo();
        Long userId = Long.valueOf(orderInfo.get("userId").toString());
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {

            throw new RuntimeException("User not found with id: " + userId);
        }

        order.setTotalPrice(Double.valueOf(orderInfo.get("totalPrice").toString()));
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setName(orderInfo.get("name").toString());
        order.setPostCode(orderInfo.get("postCode").toString());
        order.setAddress(orderInfo.get("address").toString());
        order.setDetailAddress(orderInfo.get("detailAddress").toString());
        order.setPhone(orderInfo.get("phone").toString());

        order.setTotalPrice(Double.valueOf(orderInfo.get("totalPrice").toString()));

        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) orderInfo.get("cartItemList");
        for (Map<String, Object> item : cartItems) {
            OrderProduct orderProduct = new OrderProduct();
            Long productId = Long.valueOf(item.get("productId").toString());
            orderProduct.setProduct(productRepository.findById(productId).orElse(null));
            orderProduct.setQuantity((Integer) item.get("quantity"));
            orderProduct.setSize((String) item.get("size"));
            Double price = Double.valueOf(item.get("price").toString());
            orderProduct.setPrice(price);
            orderProduct.setOrder(order);
            order.getOrderProducts().add(orderProduct);
        }

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public List<OrderListResponse> getAllOrders() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<OrderInfo> orderInfos = orderRepository.findAllWithOrderProducts(sort);

        // 모든 orderProducts에 연결된 Product들을 추출
        Set<Product> products = orderInfos.stream()
                .flatMap(o -> o.getOrderProducts().stream())
                .map(OrderProduct::getProduct)
                .collect(Collectors.toSet());

        // Product와 detailImages fetch
        List<Product> fetchedProducts = orderRepository.findProductsWithDetailImages(products);

        // orderInfos에서 사용될 수 있는 Product를 fetchedProducts로 대체
        Map<Long, Product> productMap = fetchedProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
        orderInfos.forEach(orderInfo -> orderInfo.getOrderProducts().forEach(orderProduct ->
                orderProduct.setProduct(productMap.get(orderProduct.getProduct().getId()))
        ));

        return orderInfos.stream().map(this::convertToOrderListResponse).collect(Collectors.toList());
    }



    private OrderListResponse convertToOrderListResponse(OrderInfo orderInfo) {
        OrderListResponse response = new OrderListResponse();

        response.setId(orderInfo.getId());
        response.setUser(orderInfo.getUser());
        response.setTotalPrice(orderInfo.getTotalPrice());
        response.setName(orderInfo.getName());
        response.setPostCode(orderInfo.getPostCode());
        response.setAddress(orderInfo.getAddress());
        response.setDetailAddress(orderInfo.getDetailAddress());
        response.setPhone(orderInfo.getPhone());
        response.setOrderDate(orderInfo.getOrderDate());

        List<OrderProductResponse> orderProductResponses = orderInfo.getOrderProducts().stream()
                .map(this::convertToOrderProductResponse)
                .collect(Collectors.toList());

        response.setOrderProducts(orderProductResponses);
        return response;
    }

    private OrderProductResponse convertToOrderProductResponse(OrderProduct orderProduct) {
        OrderProductResponse response = new OrderProductResponse();

        response.setId(orderProduct.getId());
        response.setProduct(convertToProductResponse(orderProduct.getProduct()));
        response.setQuantity(orderProduct.getQuantity());
        response.setSize(orderProduct.getSize());
        response.setPrice(orderProduct.getPrice());

        return response;
    }
    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setImage(product.getImage());
        response.setCreatedDate(product.getCreatedDate());
        response.setDetailImages(product.getDetailImages().stream()
                .map(this::convertToProductDetailImageResponse)
                .collect(Collectors.toList()));

        return response;
    }

    private ProductDetailImageResponse convertToProductDetailImageResponse(ProductDetailImage detailImage) {
        return new ProductDetailImageResponse(detailImage.getId(), detailImage.getDetailImageUrl());
    }



}
