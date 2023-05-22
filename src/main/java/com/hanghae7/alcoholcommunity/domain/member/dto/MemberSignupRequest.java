package com.hanghae7.alcoholcommunity.domain.member.dto;

import javax.persistence.Column;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Member Entity무결성을 위한 Dto생성
 * @fileName      : MemberSignupRequest
 * @author        : mycom
 * @since         : 2023-05-23
 */
@Getter
@ToString
public class MemberSignupRequest {

	private String memberEmailId;

	private String gender;

	private String memberName;

	private String profileImage;

	// 무결성을위해서 Builder 사용
	@Builder
	public MemberSignupRequest(String memberEmailId, String gender, String memberName, String profileImage) {
		this.memberEmailId = memberEmailId;
		this.gender = gender;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}
}
