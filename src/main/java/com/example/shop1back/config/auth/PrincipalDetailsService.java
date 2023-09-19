package com.example.shop1back.config.auth;

import com.example.shop1back.user.entity.User;
import com.example.shop1back.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//시큐리티 설정에서 loginProcessingUrl("/loginProc");
//loginProc 요청이 오면 자동으로 userDetailsService타입으로 IOC되어있는 loadUserByUsername 함수가 실행된다.

@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	//String username 자리는  input에서 name속성으로 지정한 명칭을 그대로 작성해줘야한다.
	//함수종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername메서드 진입");
		System.out.println("email:"+email);
		User user = userRepository.findByEmail(email);
		System.out.println("user@:"+user);
		System.out.println("user.getRole():"+user.getRole());
		if(user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		return new PrincipalDetails(user);
		//시큐리티 세션 <-- Authentication <--- UserDetails  
		//리턴된  new PrincipalDetails(user)값이 Authentication내부로 들어감.
		// 그리고 바로 시큐리티 세션내부에 Authentication객체가 들어감
	}

}
