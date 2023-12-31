package com.example.shop1back.user.service;

import com.example.shop1back.user.controller.form.UserRegisterForm;

public interface UserService {
    String signUp(UserRegisterForm userRegisterForm);

    boolean isEmailDuplicated(String email);

    boolean isUsernameDuplicated(String username);
}
