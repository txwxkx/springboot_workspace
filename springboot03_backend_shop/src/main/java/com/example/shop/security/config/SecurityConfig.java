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

@Configuration
@EnableWebSecurity // SpringSecurityFilterChain에 등록 /gradle에 등록해둔 jwt 사용하기 위해
@EnableGlobalMethodSecurity(securedEnabled = true) // security 사용위해 @EnableWebSecurity 와 @EnableGlobalMethodSecurity 쌍으로
                                       // 등록하기
public class SecurityConfig {
   
   @Autowired
   private MembersDAO userRepository;

   @Bean // 빈으로 선언해서 다른곳에서도 쓸 수 있도록 해둔다 / 환경설정으로 사용하려면 빈으로 설정하기
   public BCryptPasswordEncoder encodePassword() {
      return new BCryptPasswordEncoder();
   }

   @Autowired
   private CorsConfig corsConfig;
   
   @Bean
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      // csrf() : Cross site Request Forgery로 사이트간 위조 요청으로 정상적인 사용자가 의도치 않은
      // 위조 요청을 보내는 것을 의미한다.
      http.csrf().disable(); // CSRF 보호기능 비활성화
      
      // API를 사용하므로 기본으로 제공하는 formLogin()페이지 끄기
      http.formLogin().disable();
      
      //httpBasic 방식 대신 JWT를 사용하기 때문에 httpBasic() 끄기
      http.httpBasic().disable();

      
      // 세션끄기 : JWT를 사용하기 때문에 세션을 사용하지 않는다.
      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      
      //인증 사용, security Filter에 등록, @CrossOrigin (인증 x)
      http.apply(new MycustomerFilter());
      
      //요청에 의한 인가(권한)검사 시작
      http.authorizeHttpRequests()
      .antMatchers("/", "/images/**", "/login", "/board/list/**", "/member/signup").permitAll() //로그인 없이 접근 허용한다.
      .anyRequest().authenticated(); //그 외 모든 요청에 대해서 인증(로그인)이 되어야 허용한다.
      
      

      return http.build();
   }

   
   public class MycustomerFilter extends AbstractHttpConfigurer<MycustomerFilter, HttpSecurity>{
      @Override
      public void configure(HttpSecurity http) throws Exception {
         AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class); //AuthenticationManager => 인증값 관리하는 곳
         
         // @CrossOrigin(인증 x) , security Filter에 등록 인증(o)
         http.addFilter((corsConfig.corsFilter())); //응답과 관련있는 소스 / 설정안되어있을 때 콘솔에서 200에러 / CorsConfig 소스가 있어야함
         
         //addFilter() : FilterComparator에 등록되어 있는 Filter들을 활성화할 때 사용
         //addFilterBefore(), addFilterAfter() : CustomerFilter를 등록할 때 사용
         //인증 필터 등록
         http.addFilter(new JwtAuthenticationFilter(authenticationManager))
         //인가(권한) 필터 등록
         .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
      }
   }
}//end outer class

