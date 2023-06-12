package com.example.shop1back.user.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50,nullable = false , unique=true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(length=50,nullable = false)
    private String nickName;

}