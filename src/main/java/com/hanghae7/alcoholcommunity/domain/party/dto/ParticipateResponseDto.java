package com.hanghae7.alcoholcommunity.domain.party.dto;

import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateResponseDto {

	private Long memberId;
	private Long partyId;
	private Party title;

}
