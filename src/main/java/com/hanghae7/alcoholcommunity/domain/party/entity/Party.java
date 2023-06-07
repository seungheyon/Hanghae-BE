package com.hanghae7.alcoholcommunity.domain.party.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : Party
 * @author        : mycom
 * @since         : 2023-05-19
 */


@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Party extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long partyId;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String content;

	@Column
	private int totalCount;

	@ColumnDefault(value = "0")
	private int currentCount;

	@Column(nullable = false)
	@Builder.Default
	// processing을 통해 모집 중 / 모집 마감 파티 리스트 활용  recruitmentStatus
	private boolean recruitmentStatus = true;

	private String concept;
	private String hostName;

	private Double latitude;
	private Double longitude;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;

	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
	@Builder.Default
	private List<PartyParticipate> partyParticipates = new ArrayList<>();

	private String regionName;
	private String placeName;
	private String placeAddress;
	private String placeUrl;

	private double distance;
	private String stationName;
	private double distanceFromMember;

	@Column(nullable = true)
	private String imageUrl;
	public boolean isRecruitmentStatus() {
		return recruitmentStatus;
	}

	public Party(PartyRequestDto partyRequestDto, String hostName) {
			this.title = partyRequestDto.getTitle();
			this.content = partyRequestDto.getContent();
			this.concept = partyRequestDto.getConcept();
			this.latitude = partyRequestDto.getLatitude();
			this.longitude = partyRequestDto.getLongitude();
			this.hostName = hostName;
			this.createdAt = LocalDateTime.now();
			this.modifiedAt = LocalDateTime.now();
			this.partyDate = partyRequestDto.getPartyDate();
			this.totalCount = partyRequestDto.getTotalCount();
			this.placeName = partyRequestDto.getPlaceName();
			this.placeAddress = partyRequestDto.getPlaceAddress();
			this.placeUrl = partyRequestDto.getPlaceUrl();
			this.stationName =partyRequestDto.getStationName();
			this.distance = partyRequestDto.getDistance();
			this.regionName = partyRequestDto.getRegionName();
	}
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	// 모임 생성 시 자기자신 인원수 자동 +1
	public void addCurrentCount() {
		this.currentCount = currentCount +1;
	}

	// 모임 취소 시 -1
	public void subCurrentCount(){ this.currentCount = currentCount -1;	}



	// 모임 게시글 수정
	public void updateParty(PartyRequestDto partyRequestDto) {
			this.title = partyRequestDto.getTitle();
			this.content = partyRequestDto.getContent();
			this.concept = partyRequestDto.getConcept();
			this.latitude = partyRequestDto.getLatitude();
			this.longitude = partyRequestDto.getLongitude();
			this.modifiedAt = LocalDateTime.now();
			this.partyDate = partyRequestDto.getPartyDate();
			this.totalCount = partyRequestDto.getTotalCount();
			this.placeName = partyRequestDto.getPlaceName();
			this.placeAddress = partyRequestDto.getPlaceAddress();
			this.placeUrl = partyRequestDto.getPlaceUrl();
			this.stationName =partyRequestDto.getStationName();
			this.distance = partyRequestDto.getDistance();
			this.regionName = partyRequestDto.getRegionName();
		}
	public void setRecruitmentStatus(boolean recruitmentStatus){
		this.recruitmentStatus = recruitmentStatus;
	}
}
