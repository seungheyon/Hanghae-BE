package com.hanghae7.alcoholcommunity.domain.party.dto.response;

import java.util.List;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartyDetailResponseDto {
	private List<Member> participateInfo;
	private PartyResponseDto partyInfo;

	public PartyDetailResponseDto(List<Member> partyMember, PartyResponseDto partyinfo) {
		this.participateInfo = partyMember;
		this.partyInfo = partyinfo;
	}
}
