package com.hanghae7.alcoholcommunity.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeParticipantResponseDto {
    private Long partyId;
    private String partyTitle;
    private String imgUrl;
    private String participantName;
    private String participantUniqueId;
}
