package com.example.shop.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.shop.members.dao.MembersDAO;
import com.example.shop.security.jwt.JwtAuthenticationFilter;
import com.example.shop.security.jwt.JwtAuthorizationFilter;
import com.example.shop.security.service.CorsConfig;

// 황경설정용 클래스를 만듦 - xml파일은 사용이 안되므로 따로 class를 환경설정용이라고 정의한다.
@Configuration
//springSecurityFilterChain에 등록
@EnableWebSecurity 

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private MembersDAO userRepository;
	
	@Autowired
	private CorsConfig corsConfig;
	
	// bean 설정해줘야 autowired 먹는다.
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
	
	// controller 가 요청을 받기 전 filter가 먼저 처리된다. security는 filter 기반으로 dispacherservlet 보다 먼저 처리된다.
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		// csrf() : Cross Site Request Forgery로 사이트간 위조 요청으로 정상적인 사용자가 의도치 않은 위조 요청을 보내는 것을 의미한다.
		http.csrf().disable();
		
		// API를 사용하므로 기본으로 제공하는 formLogin() 페이지를 끈다.
		http.formLogin().disable();
		// httpBasic 방식대신 JWT 사용함으로 끄기
		http.httpBasic().disable();
		
//		http.authorizeHttpRequests();
		
		// 세션 끄기 : react에선 session을 처리할 수 없으므로 JWT를 사용한다. 그래서 session의 사용을 정지시킨다.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// 인증사용, security filter에 등록, @crossOrigin (인증X)
		http.apply(new MyCustomerFilter());
		
		http.authorizeHttpRequests().antMatchers("/", "/images/**", "/login", "/member/signup", "/board/list/**").permitAll() // 로그인 없이 접근 허용
		.anyRequest().authenticated(); // 그외 모든 요청은 로그인이 있어야 허용
		
		return http.build();
	}
	
	public class MyCustomerFilter extends AbstractHttpConfigurer<MyCustomerFilter, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			
			// @crossOrigin(인증 x), security Filter에 등록 인증(O)
			http.addFilter(corsConfig.corsFilter());
			
			// addFilter(): FilterComparator 에 등록되어있는 Fiter 들을 활성화할 때 사용
			// addFilterBefore(), addFilterAfter() : CustomFilter를 등록할 때 사용
			// 인증필터등록 / 인가(권한) 필터 등록
			http.addFilter(new JwtAuthenticationFilter(authenticationManager)).addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
		}
	}

}
