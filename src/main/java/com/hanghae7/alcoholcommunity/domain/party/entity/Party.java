package com.hanghae7.alcoholcommunity.domain.party.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Please explain the class!!
 *
 * @fileName      : Party
 * @author        : mycom
 * @since         : 2023-05-19
 */


@Entity
@Getter
@Setter
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
	// processing을 통해 모집 중 / 모집 마감 파티 리스트 활용  recruitmentStatus
	private boolean recruitmentStatus = true;

	private String concept;
	private String hostName;

	private Double latitude;
	private Double longitude;
	// @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	// private LocalDateTime startDate;
	// @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	// private LocalDateTime endDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;

	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<PartyParticipate> partyParticipates = new ArrayList<>();


	// 모임 신청자목록 리스트 작성?


	public Party(PartyRequestDto partyRequestDto, String hostName) {
			this.title = partyRequestDto.getTitle();
			this.content = partyRequestDto.getContent();
			this.concept = partyRequestDto.getConcept();
			this.latitude = partyRequestDto.getLatitude();
			this.longitude = partyRequestDto.getLongitude();
			this.hostName = hostName;
			// this.startDate = partyRequestDto.getStartDate();
			// this.endDate = partyRequestDto.getEndDate();
			this.partyDate = partyRequestDto.getPartyDate();
			this.totalCount = partyRequestDto.getTotalCount();
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
			// this.startDate = partyRequestDto.getStartDate();
			// this.endDate = partyRequestDto.getEndDate();
			this.partyDate = partyRequestDto.getPartyDate();
			this.totalCount = partyRequestDto.getTotalCount();
		}
}
