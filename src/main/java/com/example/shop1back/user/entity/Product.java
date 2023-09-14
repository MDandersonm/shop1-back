package com.example.shop1back.user.entity;

import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
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

}