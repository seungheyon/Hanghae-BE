package com.hanghae7.alcoholcommunity.domain.party.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.dto.Info.MemberInfoDto;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : PartyListResponseDto
 * @author        : mycom
 * @since         : 2023-05-25
 */

@Getter
@NoArgsConstructor
public class PartyListResponse {

	private Long partyId;
	private String title;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;
	private Boolean recruitmentStatus;
	private int totalCount;
	private int currentCount;
	private Double latitude;
	private Double longitude;
	private List<MemberInfoDto> memberInfo;
	private int state;
	private double distance;
	private String stationName;
	private String imageUrl;
	private String placeName;
	private String placeAddress;
	private String placeUrl;
	private double distanceFromCoordinate;
	private String regionName;

	public PartyListResponse(Party party) {
		this.partyId = party.getPartyId();
		this.title = party.getTitle();
		this.partyDate = party.getPartyDate();
		this.recruitmentStatus = party.isRecruitmentStatus();
		this.latitude = party.getLatitude();
		this.longitude = party.getLongitude();
		this.totalCount = party.getTotalCount();
		this.currentCount = party.getCurrentCount();
		this.distance = party.getDistance();
		this.stationName = party.getStationName();
		this.imageUrl = party.getImageUrl();
		this.placeAddress = party.getPlaceAddress();
		this.placeName = party.getPlaceName();
		this.placeUrl = party.getPlaceUrl();
		this.regionName = party.getRegionName();
	}
	public void setDistanceCal(double distanceFromCoordinate){
		this.distanceFromCoordinate = distanceFromCoordinate;
	}

	public PartyListResponse(Party party, int state) {
		this.partyId = party.getPartyId();
		this.title = party.getTitle();
		this.partyDate = party.getPartyDate();
		this.recruitmentStatus = party.isRecruitmentStatus();
		this.latitude = party.getLatitude();
		this.longitude = party.getLongitude();
		this.totalCount = party.getTotalCount();
		this.currentCount = party.getCurrentCount();
		this.state = state;
		this.imageUrl = party.getImageUrl();
		this.distance = party.getDistance();
		this.stationName = party.getStationName();
		this.placeAddress = party.getPlaceAddress();
		this.placeName = party.getPlaceName();
		this.placeUrl = party.getPlaceUrl();
		this.regionName = party.getRegionName();
	}

	public List<MemberInfoDto> getparticipateMembers(List<Member> participateMembers) {
		List<MemberInfoDto> memberInfos  = new ArrayList<>();
		for (Member member : participateMembers) {
			MemberInfoDto memberInfo = new MemberInfoDto();
			memberInfo.setMemberId(member.getMemberId());
			memberInfo.setMemberName(member.getMemberName());
			memberInfo.setProfileImage(member.getProfileImage());
			memberInfos.add(memberInfo);
		}
		this.memberInfo = memberInfos;
		return memberInfos;
	}

}
