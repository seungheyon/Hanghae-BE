package com.hanghae7.alcoholcommunity.domain.party.dto;

import java.util.List;

import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDetailResponseDto {
	private List<PartyParticipate> participateInfo;
	private PartyResponseDto partyInfo;
}
