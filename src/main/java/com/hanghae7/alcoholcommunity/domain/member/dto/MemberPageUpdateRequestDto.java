package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberPageUpdateRequestDto {
    private String memberName;
    private String image;
    private String characteristic;
}
