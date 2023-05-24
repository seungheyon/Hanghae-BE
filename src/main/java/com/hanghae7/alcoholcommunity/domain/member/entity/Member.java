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
}