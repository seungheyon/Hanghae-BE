package com.hanghae7.alcoholcommunity.domain.party.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartyListResponseDto {

	private List<PartyListResponse> partyList;
	private int page;
	private int totalElements;

/*	public PartyListResponseDto(List<PartyListResponse> partyList, int page){
		this.partyList = partyList;
		this.page = page;
	}*/

}
