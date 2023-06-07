package com.hanghae7.alcoholcommunity.domain.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplement implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String memberUniqueId) throws UsernameNotFoundException {

		Member member = memberRepository.findByMemberUniqueId(memberUniqueId).orElseThrow(
			()-> new UsernameNotFoundException("존재하지 않는 유저 네임입니다.")
		);
		return new UserDetailsImplement(member, member.getMemberUniqueId());
	}


}
