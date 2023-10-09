package com.example.shop1back.shopping.service;


import com.example.shop1back.shopping.service.response.KakaoApproveResponse;
import com.example.shop1back.shopping.service.response.KakaoCancelResponse;
import com.example.shop1back.shopping.service.response.KakaoReadyResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoPayService {

    private static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
//    private static final String admin_Key = System.getenv("ADMIN_KEY"); // Authorization Key
    private static final String admin_Key = "5be3fce5d42e8428c405004e609d5079"; // Authorization Key

    private KakaoReadyResponse kakaoReady;
    public KakaoReadyResponse kakaoPayReady() {
        System.out.println("kakaoPayReady서비스");
        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", "111");
        parameters.add("partner_user_id", "22");
        parameters.add("item_name", "상품명");
        parameters.add("quantity", "1");
        parameters.add("total_amount", "20000");
        parameters.add("vat_amount", "10000");
        parameters.add("tax_free_amount", "1000");
        parameters.add("approval_url", "http://localhost:8887/payment/success"); // 성공 시 redirect url
        parameters.add("cancel_url", "http://localhost:8887/payment/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://localhost:8887/payment/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);
        System.out.println(kakaoReady.getNext_redirect_pc_url());
        return kakaoReady;
    }
    public KakaoApproveResponse approveResponse(String pgToken) {
        System.out.println("approveResponse 서비스");
        System.out.println("kakaoReady.getTid()"+kakaoReady.getTid());
        System.out.println("cid"+cid);
        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid",kakaoReady.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);
        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);
        System.out.println("리턴");
        return approveResponse;
    }

    /**
     * 결제 환불
     */
    public KakaoCancelResponse kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        return cancelResponse;
    }
    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        System.out.println("getHeaders!");
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
