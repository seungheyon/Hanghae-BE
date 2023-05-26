package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

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
}
