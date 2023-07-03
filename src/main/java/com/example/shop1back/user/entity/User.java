package com.example.shop1back.user.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50,nullable = false , unique=true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(length=50,nullable = false)
    private String username;

    private String role;
    private String provider;//ex ) "google"
    private String providerId;// //gogool의 아이디넘버
    @CreationTimestamp
    private Timestamp createDate;
    @Builder
    public User( String email, String password, String username, String role, String provider, String providerId,
                Timestamp createDate) {
        super();
        this.email = email;
        this.password = password;
        this.username =username;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }

}