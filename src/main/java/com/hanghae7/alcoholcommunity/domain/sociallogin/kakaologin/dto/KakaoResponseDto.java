package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import lombok.Builder;
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

	private Long memberId;
	private String memberEmailId;

	private String memberName;

	private String profileImage;

	@Builder
	public KakaoResponseDto(Long memberId, String memberEmailId, String memberName, String profileImage) {
		this.memberId = memberId;
		this.memberEmailId = memberEmailId;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}

}
