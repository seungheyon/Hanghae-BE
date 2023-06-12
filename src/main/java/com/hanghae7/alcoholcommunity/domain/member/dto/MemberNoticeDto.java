package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberNoticeDto {
    // 클라이언트에서 파싱해서 사용하도록 partyId 전송
    private Long partyId;
    private String title;
}
