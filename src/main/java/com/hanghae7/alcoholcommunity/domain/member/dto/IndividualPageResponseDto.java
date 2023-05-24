package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class IndividualPageResponseDto {
    private String memberEmailId;
    private String memberName;
    private int age;
    private String gender;
    private String profileImage;
}
