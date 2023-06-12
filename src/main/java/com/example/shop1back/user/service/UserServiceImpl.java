package com.example.shop1back.user.service;


import com.example.shop1back.user.controller.form.UserRegisterForm;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import com.example.shop1back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    @Override
    public Boolean signUp(UserRegisterForm userRegisterForm) {
        User existingUser = userRepository.findByEmail(userRegisterForm.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("멤버가 이미 존재합니다. ");
        }

        User user = new User();
        user.setEmail(userRegisterForm.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterForm.getPassword()));//비번을 코드화해서 저장.
        user.setNickName(userRegisterForm.getNickName());
        userRepository.save(user);
        return true;
    }
}
