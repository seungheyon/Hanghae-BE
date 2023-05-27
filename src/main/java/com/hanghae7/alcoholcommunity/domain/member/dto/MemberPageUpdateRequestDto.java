package com.hanghae7.alcoholcommunity.domain.member.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberPageUpdateRequestDto {

    private String memberName;
    private int age;
}
