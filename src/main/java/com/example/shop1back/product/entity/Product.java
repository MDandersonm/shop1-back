package com.example.shop1back.product.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
    @CreatedDate  // 자동으로 생성일을 설정하기 위한 어노테이션
    @Column(updatable = false)  // 생성 후 변경 불가능하도록 설정
    private LocalDateTime createdDate;  // 등록일 필드 추가

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL )
    private List<ProductDetailImage> detailImages = new ArrayList<>();

}