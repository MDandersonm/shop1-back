package com.example.shop1back.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.shop1back.config.auth.PrincipalDetails;
import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;



//시큐리티가 filter를 가지고 있는데 그 filter중에 BasicAuthenticationFilter라는 것이 있다.
//권한이나 인증이 필요한 특정 주소를 요청했을때 위 필터를 무조건 타게 되어 있다.
//만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안타요


// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		System.out.println("인증이나 권한이 필요한 주소요청이 됨");
	}
	 
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		System.out.println("인증이나 권한이 필요한 주소요청이 됨");
		this.userRepository = userRepository;
	}

	//인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		super.doFilterInternal(request, response, chain);//반드시지워야함 이걸 안지우면 응답을 두번하게(밑에 dofilter)되서 오류가남
		System.out.println("doFilterInternal- 인증이나 권한이 필요한 주소요청이 됨");
		
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		

		
		//header가 있는지 확인
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
                        return;
		}
		System.out.println("header : "+header);
		
		//JWT토큰을 검증을 해서 정상적인 사용자인지 확인해야함
		
		String token = request.getHeader(JwtProperties.HEADER_STRING)
				.replace(JwtProperties.TOKEN_PREFIX, "");
		
		// 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
		// 내가 SecurityContext에 집적접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
		String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
				.getClaim("email").asString();
		
//		username이 있다면  	서명이 정상적으로 되었다는것
		if(email != null) {
			User user = userRepository.findByEmail(email);
			
			// 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해 
			// 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			
			//강제로 authentication 객체를 만듦
			//JWT토큰 서명을 통해서 서명이 정상이면 authentication객체를 만들어준다
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(
							principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
							null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
							principalDetails.getAuthorities());//권한을 알려줘야함
			
//			authentication객체가 실제 로그인으로 만들어진게 아니라 서명을 통한 검증으로 username이 있으면 authentication을 만들어줌
			
			// 강제로 시큐리티의 세션에 접근하여 authentication객체 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	
		chain.doFilter(request, response);
	}
	
}
