package com.hanghae7.alcoholcommunity.domain.party.dto.response;

import java.time.LocalDateTime;

import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : ApproveListDto
 * @author        : mycom
 * @since         : 2023-05-26
 */
@Getter
@NoArgsConstructor
public class ApproveListDto {

	private Long partyParticipateId;
	private Long memberId;
	private Long partyId;
	private String title;
	private LocalDateTime partyDate;
	private String memberName;
	private String memberProfileImage;
	private String reason;
	private String amountAlcohol;

	public ApproveListDto(PartyParticipate partyParticipate){
		this.partyParticipateId = partyParticipate.getId();
		this.memberId = partyParticipate.getMember().getMemberId();
		this.partyId = partyParticipate.getParty().getPartyId();
		this.title = partyParticipate.getParty().getTitle();
		this.partyDate = partyParticipate.getParty().getPartyDate();
		this.memberName = partyParticipate.getMember().getMemberName();
		this.memberProfileImage = partyParticipate.getMember().getProfileImage();
		this.reason = partyParticipate.getReason();
		this.amountAlcohol = partyParticipate.getAmountAlcohol();
	}

}
