package com.example.shop1back.user.repository;


import com.example.shop1back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByEmailAndPassword(String email, String password);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
