package com.hanghae7.alcoholcommunity.domain.member.dto;

import java.time.LocalDateTime;

import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupDto {

	private String memberEmailId;

	private String password;
	private int age;
	private String gender;
	private String memberName;



}

