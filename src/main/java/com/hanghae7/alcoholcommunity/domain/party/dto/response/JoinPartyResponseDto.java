package com.hanghae7.alcoholcommunity.domain.party.dto.response;

import java.time.LocalDateTime;

import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinPartyResponseDto {
	private Long id;
	private String title;
	private LocalDateTime partyDate;
	private int totalCount;
	private int currentCount;
	private Double latitude;
	private Double longitude;
	private boolean recruitmentStatus;
	private String profileImage;
	private String hostName;

	public JoinPartyResponseDto(PartyParticipate partyParticipate){
		this.id = partyParticipate.getParty().getPartyId();
		this.title = partyParticipate.getParty().getTitle();
		this.partyDate = partyParticipate.getParty().getPartyDate();
		this.totalCount = partyParticipate.getParty().getTotalCount();
		this.currentCount = partyParticipate.getParty().getCurrentCount();
		this.latitude = partyParticipate.getParty().getLatitude();
		this.longitude = partyParticipate.getParty().getLongitude();
		this.recruitmentStatus = partyParticipate.getParty().isRecruitmentStatus();
		this.profileImage = partyParticipate.getMember().getProfileImage();
		this.hostName = partyParticipate.getMember().getMemberName();
	}
}
