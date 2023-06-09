package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Member page update request dto.
 * 마이페이지 수정 시 요청받는 Dto
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberPageUpdateRequestDto {

    private String memberName;
    private String introduce;
}
