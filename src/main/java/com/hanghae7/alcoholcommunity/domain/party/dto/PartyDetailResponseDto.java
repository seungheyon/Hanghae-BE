package com.hanghae7.alcoholcommunity.domain.party.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDetailResponseDto {
	private List<ParticipateInfoDto> participateInfo;
	private PartyResponseDto partyInfo;
}
