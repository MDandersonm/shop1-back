package com.example.shop1back.shopping.controller;

public enum ExceptionCode {
    PAY_FAILED("Payment failed"),
    PAY_CANCEL("Payment cancelled");
    // 추가적인 코드가 필요하다면 여기에 정의

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
