package com.example.shop1back.shopping.service.request;

import lombok.Getter;

@Getter
public class KakaoPayRequest {
    private String item_name;
    private int total_amount;
    private int tax_free_amount;
}
