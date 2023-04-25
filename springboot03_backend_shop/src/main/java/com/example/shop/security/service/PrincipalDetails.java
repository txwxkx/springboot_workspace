package com.example.shop.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.shop.members.dto.MembersDTO;

public class PrincipalDetails implements UserDetails{
	
	@Autowired
	private MembersDTO membersDTO;
	
	public PrincipalDetails() {
		// TODO Auto-generated constructor stub
	}

	public PrincipalDetails(MembersDTO membersDTO) {
		this.membersDTO = membersDTO;
	}
	

	// 권한 목록을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return membersDTO.getAuthRole();
			}
		});
		return collect;
	}

	public MembersDTO getMembersDTO() {
		return membersDTO;
	}

	public void setMembersDTO(MembersDTO membersDTO) {
		this.membersDTO = membersDTO;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return membersDTO.getMemberPass();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return membersDTO.getMemberEmail();
	}

	// 계정 만료 여부 리턴 - true(만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	// 계정 잠김 여부 - true (잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	// 비밀번호(사용자 자격 증명)이 만료되었는지 여부 - true(잠기지 않음)
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	//계정 활성화 여부 - true(활성화됨)
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
