package com.example.shop1back.config;

import com.example.shop1back.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource());
        http.authorizeRequests()
                .antMatchers("/userOnly/**").authenticated()//어떠한 ROLE이든 로그인되어있으면 접근가능
                .antMatchers("/managerOnly/**").access("hasRole('ROLE_MANAGER')")
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션만드는 방식 사용 안하기
                .and()
                .formLogin()//권한이 없는 경로로 접속했을때 로그인페이지로 튕겨준다.
                .loginPage("/user/sign-up")
                .loginProcessingUrl("/loginProc")//loginProc주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
//			<form action="/loginProc" method="post">이렇게 해주면 시큐리티가 알아서 로그인 해줌 컨트롤러를 만들필요없음
                .defaultSuccessUrl("/")//성공하면 메인페이지로
                .and()
                .oauth2Login()
                .loginPage("/user/sign-up")//구글로그인이 완료된 뒤의 후처리가 필요함.
                .userInfoEndpoint()
                .userService(principalOauth2UserService);//구글로그인이 완료가되면 코드를받는게아니라 액세스토큰+사용자프로필정보를 한방에 받음

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}