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
	@Builder.Default
	private int status = 403;
	@Builder.Default
	private String msg = "Token is Expired!!!!!!!";

	public TokenMessageDto(String msg, int statusCode){
		this.msg = msg;
		this.status = statusCode;
	}
}
