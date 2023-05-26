package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author        : mycom
 * @since         : 2023-05-22
 * FE에 유저정보를 주기위한 Dto생성
 */

@Getter
@ToString
public class KakaoResponseDto {

	private Long memberId;

	private String memberUniqueId;

	private String memberName;

	private String profileImage;

	@Builder
	public KakaoResponseDto(Long memberId, String memberUniqueId, String memberName, String profileImage) {
		this.memberId = memberId;
		this.memberUniqueId = memberUniqueId;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}

}
