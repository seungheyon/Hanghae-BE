package com.hanghae7.alcoholcommunity.domain.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberSignupRequest;

@Getter
@NoArgsConstructor
@Entity(name="Member")
public class Member extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false, unique = true)
	private String memberEmailId;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String memberName;

	private String introduce;

	private int point;

	private double latitude;

	private double longitude;

	private String profileImage;

	private String authority;

	private Member(String memberEmailId, String gender, String memberName, String profileImage) {
		this.memberEmailId = memberEmailId;
		this.gender = gender;
		this.memberName = memberName;
		this.profileImage = profileImage;
	}

	/**
	 * Member Entity의 무결성을 위해 생성자 주입
	 * @param memberSignupRequest 필수적인 정보만 주입
	 * @return 신규 회원 생성
	 */
	public static Member create(MemberSignupRequest memberSignupRequest) {
		return new Member(
			memberSignupRequest.getMemberEmailId(),
			memberSignupRequest.getGender(),
			memberSignupRequest.getMemberName(),
			memberSignupRequest.getProfileImage()
		);
	}

}