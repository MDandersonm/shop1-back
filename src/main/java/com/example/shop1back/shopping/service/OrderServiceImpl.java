package com.example.shop1back.shopping.service;

import com.example.shop1back.product.repository.ProductRepository;
import com.example.shop1back.shopping.entity.OrderInfo;
import com.example.shop1back.shopping.entity.OrderProduct;
import com.example.shop1back.shopping.repository.OrderProductRepository;
import com.example.shop1back.shopping.repository.OrderRepository;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
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
}
