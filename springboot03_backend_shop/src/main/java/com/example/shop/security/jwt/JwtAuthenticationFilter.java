package com.example.shop.security.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.shop.members.dto.MembersDTO;
import com.example.shop.members.dto.User;
import com.example.shop.security.service.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

//Authentication(인증)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authManager) {
		this.authManager = authManager;
	}
	
	// http://localhost:8090/login 요청을 하면 실행 되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter -> 로그인 요청 처리 개시");
		
		try {
			// 요청에서 받아온 정보를 User class에 저장함
			ObjectMapper om = new ObjectMapper();
			MembersDTO user = om.readValue(request.getInputStream(), MembersDTO.class);
			System.out.printf("memberEmail: %s, memberPass: %s\n", user.getMemberEmail(), user.getMemberPass());
			
			// UsernamePasswordAuthenticationToken 생성 - Spring Security 프레임워크에서 사용자 이름과 비밀번호 인증 요청을 나타내기 위한 클래스. 사용자가 제공한 자격 증명을 기반으로 사용자를 인증하는 데 사용
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getMemberEmail(), user.getMemberPass());
			
			// 사용자 정보를 토큰 안에 저장
			Authentication authentication = authManager.authenticate(authenticationToken);
			System.out.println("으에엥" + authentication.getPrincipal());
			
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.printf("로그인 완료됨(인증): %s %s\n", principalDetails.getUsername(), principalDetails.getPassword());
			
			return authentication;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	// attemptAuthentication() 실행 후 인증이 정상적으로 완료되면 실행된다.
	// 여기에서 JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response해준다.
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
	      PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
	      String jwtToken = JWT.create().withSubject("mycors")
	            .withExpiresAt(new Date(System.currentTimeMillis() + (600 * 1000 * 60 * 1L)))
	            .withClaim("memberName", principalDetails.getMembersDTO().getMemberName())
	            .withClaim("authRole", principalDetails.getMembersDTO().getAuthRole())
	            .withClaim("memberEmail", principalDetails.getMembersDTO().getMemberEmail())
	            .sign(Algorithm.HMAC512("mySecurityCos"));
	      System.out.println("jwtToken: "+ jwtToken);
	      //response 응답헤더에 jwtToken 추가 
	      //Bearer 반드시 한칸 띄고
	      response.addHeader("Authorization", "Bearer "+ jwtToken);
	      final Map<String, Object> body= new HashMap<String,Object>();
	      body.put("memberName",principalDetails.getMembersDTO().getMemberName());
	      body.put("memberEmail",principalDetails.getMembersDTO().getMemberEmail());
	      body.put("authRole",principalDetails.getMembersDTO().getAuthRole());
	      ObjectMapper mapper = new ObjectMapper();
	      mapper.writeValue(response.getOutputStream(), body);
	      super.successfulAuthentication(request, response, chain, authResult);
		
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("안success");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", HttpStatus.UNAUTHORIZED.value());
        body.put("error", failed.getMessage());


        new ObjectMapper().writeValue(response.getOutputStream(), body);



		super.unsuccessfulAuthentication(request, response, failed);
	}
	
	
}
