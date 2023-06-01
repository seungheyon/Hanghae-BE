package com.hanghae7.alcoholcommunity.domain.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenMessageDto {
	private int status = 403;
	private String msg = "Token is Expired!!!!!!!";
}
