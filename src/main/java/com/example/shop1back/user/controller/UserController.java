package com.example.shop1back.user.controller;

import com.example.shop1back.user.controller.form.UserRegisterForm;
import com.example.shop1back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:8887", allowedHeaders = "*")
public class UserController {
    final private UserService userService;

    @PostMapping("/sign-up")//회원가입
    public Boolean signUp(@RequestBody UserRegisterForm form) {
        log.info("signUp(): " + form);
        return userService.signUp(form);

    }
}
