package com.example.shop1back.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/one")
    public String signUp() {
        return "hello";

    }
}
