package com.hanghae7.alcoholcommunity.domain.party.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyRequestDto {

	private String title;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;
	private String concept;
	private Double latitude;
	private Double longtitude;
	private int totalCount;
	// private int currentCount;
	private String content;

	public Party toEntity(Member member) {
		return Party.builder()
			.member(member)
			.title(title)
			.startDate(startDate)
			.endDate(endDate)
			.partyDate(partyDate)
			.concept(concept)
			.latitude(latitude)
			.longtitude(longtitude)
			.totalCount(totalCount)
			// .currentCount(currentCount)
			.content(content)
			.build();
	}

}
