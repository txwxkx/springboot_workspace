package com.example.shop.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shop.members.dao.MembersDAO;
import com.example.shop.members.dto.MembersDTO;

@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	@Autowired
	private MembersDAO membersDAO;
	
	public PrincipalDetailsService() {
		// TODO Auto-generated constructor stub
	}
	
	// 1. AuthenticationProvider에서 loadUserByName(String username)을 호출한다.
	// 2. loadUserByName(String username)에서는 DB에서 username에 해당하는 데이터를 검색한 후 UserDetails에 담는다.
	// 3. AuthenticationProvider 에서 UserDetails를 받아 Authentication에 담고 결과적으로 Security Session에 담

	@Override
	public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
		
//		System.out.println("loadUserByUsername:" + memberEmail);
		MembersDTO userEntity = membersDAO.selectByEmail(memberEmail);
		
//		System.out.println("userEntity:" + userEntity.getMemberName());
		
		if(userEntity == null) {
			throw new UsernameNotFoundException(memberEmail);
		}
		
		return new PrincipalDetails(userEntity);
	}

}
