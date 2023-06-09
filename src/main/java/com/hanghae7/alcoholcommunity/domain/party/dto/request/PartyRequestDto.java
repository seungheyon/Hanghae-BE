package com.hanghae7.alcoholcommunity.domain.party.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyRequestDto {

	private String title;
	private String content;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Seoul")
	private LocalDateTime partyDate;
	private String concept;
	private Double latitude;
	private Double longitude;
	private int totalCount;
	private String placeName;
	private String placeAddress;
	private String placeUrl;
	private double distance;
	private String stationName;
	private String regionName;

}
