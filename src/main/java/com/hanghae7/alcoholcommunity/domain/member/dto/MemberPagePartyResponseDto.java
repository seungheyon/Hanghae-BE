package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * The type Member page party response dto.
 * 마이페이지 조회 시 제공하는 사용자 참여 모임 정보 Dto
 */
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
