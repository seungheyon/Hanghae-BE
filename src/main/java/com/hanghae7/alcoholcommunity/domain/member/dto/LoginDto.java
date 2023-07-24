package com.hanghae7.alcoholcommunity.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDto {

	private String memberEmailId;

	private String password;
}
