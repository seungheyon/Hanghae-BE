package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberPagePartyResponseDto {
    private Long partyId;
    private String title;
    private String content;
    private String memberName; // 모임장 이름인지 누구 이름인지?
    private String address;
    private LocalDateTime date;
    private int totalCount;
    private int currentCount;
    private Boolean processing;
    private String profileImage;
    private LocalDateTime createdAt;
}
