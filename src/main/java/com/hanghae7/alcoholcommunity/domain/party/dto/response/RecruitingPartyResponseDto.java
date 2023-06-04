package com.hanghae7.alcoholcommunity.domain.party.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecruitingPartyResponseDto {
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
	private String regionName;

	public RecruitingPartyResponseDto(Party party, Member member){
		this.id = party.getPartyId();
		this.title = party.getTitle();
		this.partyDate = party.getPartyDate();
		this.totalCount = party.getTotalCount();
		this.currentCount = party.getCurrentCount();
		this.latitude = party.getLatitude();
		this.longitude = party.getLongitude();
		this.recruitmentStatus = party.isRecruitmentStatus();
		this.profileImage = member.getProfileImage();
		this.hostName = member.getMemberName();
		this.regionName = party.getRegionName();
	}
}
