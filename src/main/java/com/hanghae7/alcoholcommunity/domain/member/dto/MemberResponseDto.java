package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Member response dto.
 * 마이페이지 조회 시 제공하는 사용자 정보 Dto
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private String memberEmailId;
    private String memberName;
    private String profileImage;
    private String gender;
    private int age;
    private String introduce;

}
