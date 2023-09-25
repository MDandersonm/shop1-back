package com.example.shop1back.user.service;


import com.example.shop1back.user.controller.form.UserRegisterForm;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import com.example.shop1back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;

    final private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public String signUp(UserRegisterForm userRegisterForm) {
        User existingUser = userRepository.findByEmail(userRegisterForm.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("멤버가 이미 존재합니다. ");
        }

        System.out.println("회원가입 진행 : " + userRegisterForm);
        String rawPassword = userRegisterForm.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        User user = new User();
        user.setEmail(userRegisterForm.getEmail());
        user.setUsername(userRegisterForm.getUsername());
        user.setPassword(encPassword);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/loginForm";

//
//        User existingUser = userRepository.findByEmail(userRegisterForm.getEmail());
//        if (existingUser != null) {
//            throw new RuntimeException("멤버가 이미 존재합니다. ");
//        }
//
//        User user = new User();
//        user.setEmail(userRegisterForm.getEmail());
//        user.setPassword(passwordEncoder.encode(userRegisterForm.getPassword()));//비번을 코드화해서 저장.
//        user.setNickName(userRegisterForm.getNickName());
//        userRepository.save(user);
//        return true;
    }

    @Override
    public boolean isEmailDuplicated(String email) {
        return userRepository.opFindByEmail(email).isPresent();
    }

    @Override
    public boolean isUsernameDuplicated(String username) {
        return userRepository.opFindByUsername(username).isPresent();
    }
}
