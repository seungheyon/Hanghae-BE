package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto;

import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoAccount;

import lombok.Builder;
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

	private String memberEmailId;

	private String memberName;

	private String profileImage;

	@Builder
	public NaverResponseDto(String memberEmailId, String memberName, String profileImage) {
		this.memberEmailId = memberEmailId;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}

}
