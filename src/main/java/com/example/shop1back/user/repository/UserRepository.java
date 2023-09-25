package com.example.shop1back.user.repository;


import com.example.shop1back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> opFindByEmail(String email);

    User findByUsername(String username);
    User findByEmailAndPassword(String email, String password);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> opFindByUsername(String username);
}
