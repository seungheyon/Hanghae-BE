package com.hanghae7.alcoholcommunity.domain.common.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

public class UserDetailsImplement implements UserDetails {

	private final Member member;
	private final String memberUniqueId;
	public UserDetailsImplement(Member member, String memberUniqueId) {
		this.member = member;
		this.memberUniqueId = memberUniqueId;
	}
	public Member getMember(){
		return member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String authority = member.getAuthority();
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.memberUniqueId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
