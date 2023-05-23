package com.hanghae7.alcoholcommunity.domain.party.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : example
 * @author        : mycom
 * @since         : 2023-05-19
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyResponseDto {

	private Long partyId;
	private String title;
	private String content;
	private String profileImage;
	private Long memberId;
	private String memberName;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime endDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;
	private Double latitude;
	private Double longtitude;
	private Boolean processing;
	private int totalCount;
	private int currentCount;
	@JsonFormat(shape = JsonFormat.Shape.STRING,timezone= "Asia/Seoul")
	private LocalDateTime modifiedAt;

	public PartyResponseDto(Party party) {
		this.partyId = party.getPartyId();
		this.title = party.getTitle();
		this.content = party.getContent();
		this.profileImage = party.getMember().getProfileImage();
		this.memberId = party.getMember().getMemberId();
		this.memberName = party.getMember().getMemberName();
		this.startDate = party.getStartDate();
		this.endDate = party.getEndDate();
		this.partyDate = party.getPartyDate();
		this.latitude = party.getLatitude();
		this.longtitude = party.getLongtitude();
		this.totalCount = party.getTotalCount();
		this.currentCount = party.getCurrentCount();
		this.modifiedAt = party.getModifiedAt();
	}
}
