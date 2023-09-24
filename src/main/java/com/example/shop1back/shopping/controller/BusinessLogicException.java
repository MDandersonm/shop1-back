package com.example.shop1back.shopping.controller;

public class BusinessLogicException extends Exception {

    public BusinessLogicException(ExceptionCode code) {
        super(code.getMessage());
    }
}
