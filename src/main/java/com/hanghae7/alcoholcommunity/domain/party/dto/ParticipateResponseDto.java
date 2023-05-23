package com.hanghae7.alcoholcommunity.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateResponseDto {

	private Long memberId;
	private Long partyId;
}
