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
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : example
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member member;

	@Column
	private int totalCount;
	@ColumnDefault(value = "0")
	private int currentCount;

	private String concept;

	private Double latitude;
	private Double longtitude;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime endDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;

	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
	List<PartyParticipate> partyParticipates = new ArrayList<>();



	@Builder
	public Party(PartyRequestDto partyRequestDto, Member member) {
		// PartyValidator.isValidParty(member, title, content, latitude, longtitude, startDate, endDate, partyDate, totalCount, currentCount);
			this.member = member;
			this.title = partyRequestDto.getTitle();
			this.content = partyRequestDto.getContent();
			this.concept = partyRequestDto.getConcept();
			this.latitude = partyRequestDto.getLatitude();
			this.longtitude = partyRequestDto.getLongtitude();
			this.startDate = partyRequestDto.getStartDate();
			this.endDate = partyRequestDto.getEndDate();
			this.partyDate = partyRequestDto.getPartyDate();
			this.totalCount = partyRequestDto.getTotalCount();
			// this.currentCount = partyRequestDto.getCurrentCount();
	}

	public void addCurrentCount() {
		this.currentCount = currentCount +1;
	}


}
