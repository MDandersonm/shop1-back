package com.example.shop1back.config;

import com.example.shop1back.config.jwt.JwtAuthenticationFilter;
import com.example.shop1back.config.jwt.JwtAuthorizationFilter;
import com.example.shop1back.config.oauth.PrincipalOauth2UserService;
import com.example.shop1back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())//
                .csrf().disable()//
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and()
                .formLogin().disable()//
                .httpBasic().disable()//


                .addFilter(new JwtAuthenticationFilter(authenticationManager()))//
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))//
                .authorizeRequests()
                .antMatchers("/**/onlyuser/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/**/onlyadmin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
//                .and()
//                 OAuth2 Login 설정
//                .oauth2Login()
//                .loginPage("/login")
//                .userInfoEndpoint()
//                .userService(principalOauth2UserService);
    }


//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.cors().configurationSource(corsConfigurationSource());
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
////                .addFilter(corsFilter)
//                .formLogin().disable()
//                .httpBasic().disable()
//                .addFilter(new JwtAuthenticationFilter(authenticationManager()))//파라미터 : AuthenticationManager
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))
//                .authorizeRequests()
//                .antMatchers("/userOnly/**").authenticated()//어떠한 ROLE이든 로그인되어있으면 접근가능(사용자가 로그인 상태인 경우)
//                .antMatchers("/managerOnly/**").access("hasRole('ROLE_MANAGER')")
//                .anyRequest().permitAll();
//
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


}