package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The type Individual page response dto.
 * 상대 페이지 조회 시 제공하는 사용자 정보 Dto
 */
@Getter
@AllArgsConstructor
@Builder
public class IndividualPageResponseDto {
    private String memberEmailId;
    private String memberName;
    private int age;
    private String gender;
    private String profileImage;
    private String introduce;
}
