package com.example.shop1back.shopping.entity;

import com.example.shop1back.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderInfo order;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private String size;
    private Double price;


}