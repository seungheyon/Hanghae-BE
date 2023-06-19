package com.hanghae7.alcoholcommunity.domain.notification.dto;

import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
	// 클라이언트에서 파싱해서 사용하도록 partyId 전송
	private Long partyId;
	private String partyTitle;
	private Boolean accepted;

	public NoticeResponseDto(Notice notice) {
		this.partyId = notice.getPartyId();
		this.partyTitle = notice.getPartyTitle();
		this.accepted = notice.getAccepted();
	}
}
