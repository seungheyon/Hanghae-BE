package com.hanghae7.alcoholcommunity.domain.member.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberSignupRequest;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

@Getter
@NoArgsConstructor
@Entity(name="Member")
public class Member extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false)
	private String memberEmailId;

	@Column(nullable = false, unique = true)
	private String memberUniqueId;

	@Column(nullable = true)
	private int age;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String memberName;

	private String introduce;

	private int point;

	private double latitude;

	private double longitude;

	private String profileImage;

	private String authority = "user";

	private String social;

	private LocalDateTime createdAt;



	private Member(String memberEmailId, String memberUniqueId, String gender, String memberName, String profileImage, String social, LocalDateTime createdAt) {
		this.memberEmailId = memberEmailId;
		this.memberUniqueId = memberUniqueId;
		this.gender = gender;
		this.memberName = memberName;
		this.profileImage = profileImage;
		this.social = social;
		this.createdAt = createdAt;
	}

	/**
	 * Member Entity의 무결성을 위해 생성자 주입
	 * @param memberSignupRequest 필수적인 정보만 주입
	 * @return 신규 회원 생성
	 */
	public static Member create(MemberSignupRequest memberSignupRequest) {
		return new Member(
			memberSignupRequest.getMemberEmailId(),
			memberSignupRequest.getMemberUniqueId(),
			memberSignupRequest.getGender(),
			memberSignupRequest.getMemberName(),
			memberSignupRequest.getProfileImage(),
			memberSignupRequest.getSocial(),
			memberSignupRequest.getCreatedAt()
		);
	}

	public void update(String newMemberName ,String newProfileImage){
		this.memberName = newMemberName;
		this.profileImage = newProfileImage;
	}

	public void update(String newMemberName){
		this.memberName = newMemberName;
	}



}
