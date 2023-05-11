package com.example.shop.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.shop.members.dto.MembersDTO;

public class PrincipalDetails implements UserDetails {
	
	private MembersDTO membersDTO;
	
	public PrincipalDetails() {

	}

	public PrincipalDetails(MembersDTO membersDTO) {
		this.membersDTO = membersDTO;
	}
	
	public MembersDTO getMembersDTO() {
		return membersDTO;
	}

	//권한 목록을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return membersDTO.getAuthRole();
			}
		});
		
		return collect;
	}

	@Override
	public String getPassword() {
		
		return membersDTO.getMemberPass();
	}

	@Override
	public String getUsername() {
		
		return membersDTO.getMemberEmail();
	}

	//계정의 만료 여부 리턴 - true(만료 안됨)
	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	//계정의 잠김 여부 리턴 - true(잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	//비밀번호의 잠김 여부 리턴 - true(잠기지 않음)
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	//계정의 활성화 여부 리턴 - true(활성화 됨)
	@Override
	public boolean isEnabled() {
		
		return true;
	}
	
}//end class
