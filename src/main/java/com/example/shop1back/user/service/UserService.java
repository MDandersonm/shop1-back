package com.example.shop1back.user.service;

import com.example.shop1back.user.controller.form.UserRegisterForm;

public interface UserService {
    Boolean signUp(UserRegisterForm userRegisterForm);
}
