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

	private Long memberId;

	private String memberUniqueId;

	private String memberName;

	private String profileImage;

	@Builder
	public NaverResponseDto(Long memberId, String memberUniqueId, String memberName, String profileImage) {
		this.memberId = memberId;
		this.memberUniqueId = memberUniqueId;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}

}
