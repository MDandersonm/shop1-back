package com.example.shop1back.user.controller;

import com.example.shop1back.config.auth.PrincipalDetails;
import com.example.shop1back.user.controller.form.UserRegisterForm;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:8887", allowedHeaders = "*")
public class UserController {
    final private UserService userService;

    @PostMapping("/sign-up")//회원가입
    public String signUp(@RequestBody UserRegisterForm form) {
        log.info("signUp(): " + form);
        return userService.signUp(form);
    }
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/onlyuser/userinfo")
    public User user(Authentication authentication) {
        System.out.println("userinfo에 접근");
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal : "+principal.getUser().getId());
        System.out.println("principal : "+principal.getUser().getUsername());
        System.out.println("principal : "+principal.getUser().getPassword());
        System.out.println("principal : "+principal.getUser().getRole());
        return principal.getUser();
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails) {	//의존성주입
        System.out.println("test/login===========");
        PrincipalDetails principalDetails =(PrincipalDetails)authentication.getPrincipal();
        ///다운케스팅을 거쳐서 user오브젝트를 찾는방법
        System.out.println("principalDetails.getUser():"+ principalDetails.getUser());
        System.out.println("authentication.getPrincipal():"+ authentication.getPrincipal());

        //@authentication어노테이션을 통해서 user오브젝트를 찾는 방법
        System.out.println("userDetails.getUsername():"+userDetails.getUsername());
        System.out.println("userDetails.getUser():"+userDetails.getUser());

        //다운케스팅을 거쳐서 user오브젝트를 찾을수도있고
        //@authentication 어노테이션을통해서도 찾을수있다 두가지방법이있는거.
        return "세션 정보 확인하기";
    }



}
