package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * @author        : mycom
 * @since         : 2023-05-22
 * 프론트엔드에 주는 유저정보에 kakao에 로그아웃하기위한 accesstoken을 전달하기 위함
 */

@Getter
@ToString
public class KakaoResponseDto {
	private KakaoAccount kakaoAccount;
	private String kakaoaccessToken;

	/**
	 *
	 * @param kakaoAccount FE에 주기위한 유저정보
	 * @param accessToken  FE 주기위한 accessToken
	 */
	public KakaoResponseDto(KakaoAccount kakaoAccount, String accessToken) {

		this.kakaoAccount = kakaoAccount;
		this.kakaoaccessToken = accessToken;
	}

}
