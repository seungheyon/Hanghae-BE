package com.hanghae7.alcoholcommunity.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbsenceNoticeDto {
    private Long noticeId;
    private Long partyId;
    private String partyTitle;
    private Integer noticeCode;
    private Boolean isRead;
}
