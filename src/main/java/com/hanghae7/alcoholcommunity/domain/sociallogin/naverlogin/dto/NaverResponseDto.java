package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto;

import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoAccount;

import lombok.Getter;
import lombok.ToString;

/**
 * Please explain the class!!
 *
 * @fileName      : NaverResponseDto
 * @author        : mycom
 * @since         : 2023-05-22
 */
@Getter
@ToString
public class NaverResponseDto {

	private Response response;
	private String naverAccessToken;

	/**
	 *
	 * @param response FE에 주기위한 유저정보
	 * @param accessToken  FE 주기위한 accessToken
	 */
	public NaverResponseDto(Response response, String accessToken) {

		this.response = response;
		this.naverAccessToken = accessToken;
	}

}
